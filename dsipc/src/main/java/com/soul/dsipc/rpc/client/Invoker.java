package com.soul.dsipc.rpc.client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;

public class Invoker implements InvocationHandler{

	InetSocketAddress address;
	RpcClient client;

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		
		// Build RpcRequest
		this.client.call();
		return null;
	}
	
}
