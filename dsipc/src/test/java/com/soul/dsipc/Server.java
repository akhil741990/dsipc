package com.soul.dsipc;

import java.io.IOException;

import com.soul.dsipc.server.RpcServer;

public class Server {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		RpcServer server = RpcServer.getServerInstance("localhost", 5555);
		
		server.start();
		
	}

}
