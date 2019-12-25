package com.soul.dsipc.rpc.client;

import java.net.InetSocketAddress;

import com.soul.dsipc.rpc.packet.Writable;

public class Call {
	
	final Writable rpcRequest;
	Writable rpcResponse; 
	public Writable getRpcResponse() {
		return rpcResponse;
	}
	public void setRpcResponse(Writable rpcResponse) {
		this.rpcResponse = rpcResponse;
	}
	final InetSocketAddress address;
	public Call(Writable rpcRequest, InetSocketAddress address){
		this.rpcRequest = rpcRequest;
		this.address = address;
	}

}
