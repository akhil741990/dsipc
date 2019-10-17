package com.soul.dsipc.rpc.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class Connection {

	private Socket socket;
	private DataInputStream in;
	private DataOutputStream out;
	
	public void sendRpcRequest(){
		
		// write the serialized RpcRequest to out
	}
}
