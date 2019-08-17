package com.soul.dsipc.server.net;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/*
 * This class will be responsible for reading the network bytes from the client
 * channel and build the RpcRequest
 */
public class RpcConnectionHandler {

	private SocketChannel channel;
	private ByteBuffer data;
	private ByteBuffer header;
	
	public RpcConnectionHandler(SocketChannel channel) {
		this.channel = channel;
	}
	
	
	public void readRpcRequest(){
		
	}
}
