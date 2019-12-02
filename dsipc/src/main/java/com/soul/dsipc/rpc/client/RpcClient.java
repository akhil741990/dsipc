package com.soul.dsipc.rpc.client;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import com.soul.dsipc.rpc.Utils;
import com.soul.dsipc.rpc.packet.DataOutputBuffer;
import com.soul.hadoop.common.protobuf.RpcHeaderProtos.RpcRequestHeaderProto;
import com.soul.hadoop.common.protobuf.RpcHeaderProtos.RpcRequestHeaderProto.OperationProto;

public class RpcClient {

	Socket conn;
	Map<String,Socket> responseMapper;
	
	public RpcClient(){
		responseMapper = new HashMap<String, Socket>();
	}
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
		  Connection conn = new Connection();
		  conn.sendRpcRequest(call);
	}
	
	
}
