package com.soul.dsipc.rpc.client;

import java.net.InetSocketAddress;

import com.soul.dsipc.rpc.packet.Writable;

public class Call {
	
	final Writable rpcRequest;
	final InetSocketAddress address;
	public Call(Writable rpcRequest, InetSocketAddress address){
		this.rpcRequest = rpcRequest;
		this.address = address;
	}

}
