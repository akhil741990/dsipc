package com.soul.dsipc.rpc;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;

import com.google.protobuf.Message;
import com.soul.dsipc.rpc.client.RpcClient;
import com.soul.dsipc.rpc.packet.RpcRequestWrapper;
import com.soul.hadoop.common.protobuf.ProtobufRpcEngineProtos.RequestHeaderProto;
import com.soul.hadoop.common.protobuf.RpcHeaderProtos.RpcRequestHeaderProto;

public class Invoker implements InvocationHandler{

	InetSocketAddress address;
	RpcClient client;
    String protocolName;
    private final long clientProtocolVersion;
	public Invoker(Class<?> protocol,long clientProtocolVersion){
		this.protocolName = protocol.getSimpleName();
		this.clientProtocolVersion = clientProtocolVersion;
	}
	
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		
		// Build RpcRequest
		
		/*Add proto file for RpcRequestHeader
		 Header will be constructed from method
		 Body will be constructed form args[1]
		*/ 
		RequestHeaderProto header = constructRpcRequestHeader(method);
		Message message = (Message) args[1];
		
		RpcRequestWrapper wrapper = new RpcRequestWrapper(header, message);
		
		this.client.call();
		return null;
	}
	
	
	 private RequestHeaderProto constructRpcRequestHeader(Method method) {
		RequestHeaderProto.Builder builder = RequestHeaderProto.newBuilder();
	      builder.setMethodName(method.getName());
	      builder.setDeclaringClassProtocolName(protocolName);
	      builder.setClientProtocolVersion(clientProtocolVersion);
	      return builder.build();
	    }
}
