package com.soul.dsipc.rpc.client;

public class ProtocolProxy<T> {
	private Class<T> protocol;
	T proxy;
	public ProtocolProxy(Class<T> protocol, T proxy){
		this.protocol = protocol;
		this.proxy = proxy;
	}
}
