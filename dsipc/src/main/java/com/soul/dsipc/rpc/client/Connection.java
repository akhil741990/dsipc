package com.soul.dsipc.rpc.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.Channel;
import java.nio.channels.SocketChannel;

import com.google.protobuf.ByteString;
import com.google.protobuf.CodedOutputStream;
import com.soul.dsipc.rpc.packet.DataOutputBuffer;
import com.soul.dsipc.rpc.packet.RpcResponseWrapper;
import com.soul.hadoop.common.protobuf.RpcHeaderProtos.RpcKindProto;
import com.soul.hadoop.common.protobuf.RpcHeaderProtos.RpcRequestHeaderProto;
import com.soul.hadoop.common.protobuf.RpcHeaderProtos.RpcRequestHeaderProto.OperationProto;
import com.soul.hadoop.common.protobuf.RpcHeaderProtos.RpcResponseHeaderProto;
import com.soul.hadoop.common.protobuf.RpcHeaderProtos.RpcResponseHeaderProto.RpcStatusProto;

public class Connection {

	private SocketChannel channel;
	private DataInputStream in;
	private DataOutputStream out;
	
	public void sendRpcRequest(Call call) throws IOException{
		
		// Serialize the call to be sent. This is done from the actual
	      // caller thread, rather than the sendParamsExecutor thread,
	      
	      // so that if the serialization throws an error, it is reported
	      // properly. This also parallelizes the serialization.
	      //
	      // Format of a call on the wire:
	      // 0) Length of rest below (1 + 2)
	      // 1) RpcRequestHeader  - is serialized Delimited hence contains length
	      // 2) RpcRequest
	      //
	      // Items '1' and '2' are prepared here. 
	      final DataOutputBuffer d = new DataOutputBuffer();
	      
	      RpcRequestHeaderProto.Builder result = RpcRequestHeaderProto.newBuilder();
	      result.setRpcOp(OperationProto.RPC_FINAL_PACKET).setCallId(1)
	          .setClientId(ByteString.copyFrom("MyClinet".getBytes()));
	     
	      
	      
//	      RpcRequestHeaderProto.Builder result = RpcRequestHeaderProto.newBuilder();
//	      result.setRpcKind(convert(rpcKind)).setRpcOp(operation).setCallId(callId)
//	          .setRetryCount(retryCount).setClientId(ByteString.copyFrom(uuid));
	      
	      
	      RpcRequestHeaderProto header = result.build();
	      header.writeDelimitedTo(d);
	      call.rpcRequest.write(d);
	      
	      
	      if(setUpConnection(call.address)){
	    	  setUpIoStreams();
	    	  byte[] data = d.getData();
              int totalLength = d.getLength();
              out.writeInt(totalLength); // Total Length
              out.write(data, 0, totalLength);// RpcRequestHeader + RpcRequest
              out.flush();
	      }else{
	    	throw new IOException("Unable to connect to Server"); 
	      }
	      
	      
	      readResponse(call);
	      close();
	      // Read Response
	}
	
	private boolean setUpConnection(InetSocketAddress address) throws IOException{
		
		//TODO : use socket factory
		channel = SocketChannel.open();
		channel.configureBlocking(true);
		channel.connect(address);
		return channel.isConnected();
	}
	
	
	private void setUpIoStreams() throws IOException{
		this.out = new DataOutputStream(channel.socket().getOutputStream());
		this.in = new DataInputStream(channel.socket().getInputStream());
	}
	
	private void readResponse(Call call){
		
        RpcResponseHeaderProto header;
		try {
			int totalLen = in.readInt();
			header = RpcResponseHeaderProto.parseDelimitedFrom(in);
			int headerLen = header.getSerializedSize();
	        headerLen += CodedOutputStream.computeRawVarint32Size(headerLen);
	        
	        RpcStatusProto status = header.getStatus();
	        System.out.println("RpcResponseStatus :" + status);
	        RpcResponseWrapper respWrapper = new RpcResponseWrapper();
	        respWrapper.readFields(in);	        
	        call.setRpcResponse(respWrapper);
	        
	        //System.out.println("Response : " + respWrapper);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        
	}
	
	public void close() throws IOException{
		channel.close();
	}
	
}
