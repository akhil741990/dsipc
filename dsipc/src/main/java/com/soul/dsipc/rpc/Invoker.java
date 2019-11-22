package com.soul.dsipc.rpc;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;

import com.soul.dsipc.rpc.client.RpcClient;
import com.soul.hadoop.common.protobuf.ProtobufRpcEngineProtos.RequestHeaderProto;
import com.soul.hadoop.common.protobuf.RpcHeaderProtos.RpcRequestHeaderProto;

public class Invoker implements InvocationHandler{

	InetSocketAddress address;
	RpcClient client;
    String protocolName;
	public Invoker(Class<?> protocol){
		this.protocolName = protocol.getSimpleName();
	}
	
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		
		// Build RpcRequest
		
		/*Add proto file for RpcRequestHeader
		 Header will be constructed from method
		 Body will be constructed form args[1]
		*/ 
		
		
		
		
		this.client.call();
		return null;
	}
	
	
	 private RequestHeaderProto constructRpcRequestHeader(Method method,long clientProtocolVersion) {
		 RequestHeaderProto.Builder builder = RequestHeaderProto
		          .newBuilder();
		      builder.setMethodName(method.getName());
		      builder.setDeclaringClassProtocolName(protocolName);
		      builder.setClientProtocolVersion(clientProtocolVersion);
		      return builder.build();
	    }
}
