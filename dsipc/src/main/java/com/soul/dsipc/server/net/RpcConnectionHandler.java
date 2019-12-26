package com.soul.dsipc.server.net;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import com.google.protobuf.Message;
import com.google.protobuf.Message.Builder;
import com.soul.dsipc.rpc.packet.RpcRequestWrapper;
import com.soul.dsipc.server.RpcCall;
import com.soul.dsipc.server.RpcServer;
import com.soul.hadoop.common.protobuf.RpcHeaderProtos.RpcRequestHeaderProto;

/*
 * This class will be responsible for reading the network bytes from the client
 * channel and build the RpcRequest
 */
public class RpcConnectionHandler {

	private SocketChannel channel;
	private ByteBuffer dataBuffer;
	private ByteBuffer headerBuffer;
	private ByteBuffer dataLengthBuffer;
	private SelectionKey key;
	public SelectionKey getKey() {
		return key;
	}


	public void setKey(SelectionKey key) {
		this.key = key;
	}


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
		
		Header ============================================== ????
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
//			count = this.channel.read(headerBuffer);
//			if(count < 0){
//				return;
//			}
//			int version  = headerBuffer.get(0);
//			int serviceClass = headerBuffer.get(1);
			
			
			dataLengthBuffer.flip();
			//Reading data
			dataLength = dataLengthBuffer.getInt();
			this.dataBuffer = ByteBuffer.allocate(dataLength);
			count = this.channel.read(dataBuffer);
			
			final DataInputStream dis = new DataInputStream(new ByteArrayInputStream(dataBuffer.array()));
		    final RpcRequestHeaderProto header = decodeProtoBuff(RpcRequestHeaderProto.newBuilder(), dis);
			
		    //TODO :  validate header
		    
		    RpcRequestWrapper wr = new RpcRequestWrapper();
		    wr.readFields(dis);
		    
		    RpcCall c = new RpcCall(wr, channel);
		    RpcServer.getServerInstance(null, 0).getQueue().add(c);
		    
		    key.cancel(); // un registering the client
		    //TODO : If in future a lock is added for selection the cancel call must be synchronized
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public <T extends Message> T decodeProtoBuff(Builder builder, DataInputStream is){
		
		try {
		 builder.mergeDelimitedFrom(is);
		 return (T)builder.build();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	
}
