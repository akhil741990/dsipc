package com.soul.dsipc.rpc.client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;

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
	
}
