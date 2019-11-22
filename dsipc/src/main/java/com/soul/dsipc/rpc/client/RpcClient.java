package com.soul.dsipc.rpc.client;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class RpcClient {

	Socket conn;
	Map<String,Socket> responseMapper;
	
	public RpcClient(){
		responseMapper = new HashMap<String, Socket>();
	}
	public void sendRpcRequest(Call call){

		// create socket
		// write call.rpcRequest to OutputStream
		
		
		
	}
	
	
}
