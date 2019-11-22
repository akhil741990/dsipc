package com.soul.dsipc.rpc.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import com.google.protobuf.ByteString;
import com.soul.dsipc.rpc.packet.DataOutputBuffer;
import com.soul.hadoop.common.protobuf.RpcHeaderProtos.RpcRequestHeaderProto;
import com.soul.hadoop.common.protobuf.RpcHeaderProtos.RpcRequestHeaderProto.OperationProto;

public class Connection {

	private Socket socket;
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
	      result.setRpcOp(OperationProto.RPC_FINAL_PACKET).setCallId(0)
	          .setClientId(ByteString.copyFrom("MyClinet".getBytes()));
	      
	      
	      RpcRequestHeaderProto header = result.build();
	      header.writeDelimitedTo(d);
	      call.rpcRequest.write(d);
	      
	      
	      // Write d to socket out put stream
	}
}
