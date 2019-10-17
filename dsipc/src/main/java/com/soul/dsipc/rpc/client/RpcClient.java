package com.soul.dsipc.rpc.client;

public class RpcClient {

	Connection conn;
	public void call(){
		this.conn.sendRpcRequest(); 
	}
}
