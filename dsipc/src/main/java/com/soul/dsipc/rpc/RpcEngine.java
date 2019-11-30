package com.soul.dsipc.rpc;

import java.io.IOException;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;

public class RpcEngine {

	
	
	public static <T> ProtocolProxy<T> getProxy(Class<T> protocol, long clientVersion,
		      InetSocketAddress addr) throws IOException {
				
		Invoker i = new Invoker(protocol, clientVersion, addr);
		return new ProtocolProxy<T>(protocol, 
				(T)Proxy.newProxyInstance(protocol.getClassLoader(), new Class[]{protocol}, i));
		
		    
	}
	
	
	
	
	
}
