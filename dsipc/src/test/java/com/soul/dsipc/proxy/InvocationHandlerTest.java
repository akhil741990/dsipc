package com.soul.dsipc.proxy;

import java.lang.reflect.Proxy;

public class InvocationHandlerTest {

	public static void main(String args[]){
		DummyProtocolImpl imp = new DummyProtocolImpl();
		
		DummProtocol proto = (DummProtocol)Proxy.newProxyInstance(imp.getClass().getClassLoader(), new Class<?>[]{DummProtocol.class}, new ProxyInvocationHandler(imp));
	
		String version  = proto.getVersion();
	
		
		System.out.println("Ouput : " +  version);
		
	}
}
