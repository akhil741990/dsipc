package com.soul.dsipc.server.net;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/*
 * This class will be responsible for reading the network bytes from the client
 * channel and build the RpcRequest
 */
public class RpcConnectionHandler {

	private SocketChannel channel;
	private ByteBuffer dataBuffer;
	private ByteBuffer headerBuffer;
	private ByteBuffer dataLengthBuffer;
	private int dataLength;
	public RpcConnectionHandler(SocketChannel channel) {
		this.channel = channel;
		headerBuffer = ByteBuffer.allocate(3);
		dataLengthBuffer = ByteBuffer.allocate(4);
	}
	
	
	/*
	 * 
		DataLength :
		 First 4 bytes  
		
		Header
		Next 3 bytes are header :
				1st byte = version
				2nd byte = Service class
				3rd byte = Authentication params
		Data : 
			Next dataLength Bytes

	 */
	public void readRpcRequest(){
	
		
		try {
			//Reading data length
			int count = this.channel.read(dataLengthBuffer);
			
			if(count < 0){
				return;
			}
			
			//Reading Header
			count = this.channel.read(headerBuffer);
			if(count < 0){
				return;
			}
			
			//Reading data
			dataLength = dataLengthBuffer.getInt();
			this.dataBuffer = ByteBuffer.allocate(dataLength);
			count = this.channel.read(dataBuffer);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
