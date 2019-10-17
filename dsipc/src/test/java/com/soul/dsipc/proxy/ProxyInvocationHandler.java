package com.soul.dsipc.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ProxyInvocationHandler implements InvocationHandler{

	private Object object;
	
	public ProxyInvocationHandler(Object object) {
		this.object = object;
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		// TODO Auto-generated method stub
		System.out.println("Method invoked :" + method.getName());
		
		// So in our usecase we need to construct and RpcRequest using the method and args and 
		// send the request to the RpcServer
		return method.invoke(object, args);
	}

}
