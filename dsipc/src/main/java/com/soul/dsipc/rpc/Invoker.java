package com.soul.dsipc.rpc;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.google.protobuf.Message;
import com.soul.dsipc.rpc.client.Call;
import com.soul.dsipc.rpc.client.RpcClient;
import com.soul.dsipc.rpc.packet.RpcRequestWrapper;
import com.soul.dsipc.rpc.packet.RpcResponseWrapper;
import com.soul.hadoop.common.protobuf.ProtobufRpcEngineProtos.RequestHeaderProto;
import com.soul.hadoop.common.protobuf.RpcHeaderProtos.RpcRequestHeaderProto;

public class Invoker implements InvocationHandler {

	InetSocketAddress address;
	RpcClient client;
	String protocolName;
	private final long clientProtocolVersion;
	private final Map<String, Message> returnTypes = new ConcurrentHashMap<String, Message>();

	public Invoker(Class<?> protocol, long clientProtocolVersion, InetSocketAddress address) {
		this.protocolName = protocol.getSimpleName();
		this.clientProtocolVersion = clientProtocolVersion;
		this.address = address;
		this.client = new RpcClient();
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

		// Build RpcRequest

		/*
		 * Add proto file for RpcRequestHeader Header will be constructed from
		 * method Body will be constructed form args[1]
		 */
		RequestHeaderProto header = constructRpcRequestHeader(method, clientProtocolVersion);
		Message message = (Message) args[1];

		RpcRequestWrapper wrapper = new RpcRequestWrapper(header, message);

		Call call = new Call(wrapper, address);
		this.client.sendRpcRequest(call);
		
		RpcResponseWrapper resp = (RpcResponseWrapper) call.getRpcResponse();
		
		
		Message prototype = getReturnProtoType(method);

		Message respMessage = prototype.newBuilderForType().mergeFrom(resp.getTheResponseRead()).build();
		
		return respMessage;
	}

	private RequestHeaderProto constructRpcRequestHeader(Method method, long clientProtocolVersion) {
		RequestHeaderProto.Builder builder = RequestHeaderProto.newBuilder();
		builder.setMethodName(method.getName());
		builder.setDeclaringClassProtocolName(protocolName);
		builder.setClientProtocolVersion(clientProtocolVersion);
		return builder.build();
	}

	private Message getReturnProtoType(Method method) throws Exception {
		if (returnTypes.containsKey(method.getName())) {
			return returnTypes.get(method.getName());
		}

		Class<?> returnType = method.getReturnType();
		Method newInstMethod = returnType.getMethod("getDefaultInstance");
		newInstMethod.setAccessible(true);
		Message prototype = (Message) newInstMethod.invoke(null, (Object[]) null);
		returnTypes.put(method.getName(), prototype);
		return prototype;
	}
}
