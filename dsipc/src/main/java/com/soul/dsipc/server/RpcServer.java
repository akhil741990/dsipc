package com.soul.dsipc.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class RpcServer {

	private String ip;
	private int port;
	private ServerSocketChannel serverSocket;
	private Selector listenerSelector;
	private Listener listener;
	private Reader reader;
	private ConnectionRegistrar connRegistrar;
	private BlockingQueue<SocketChannel> connRegistrarQ;
	
	public RpcServer(String ip, int port){
		this.ip =  ip;
		this.port = port;
	}
	
	private void initilaize() throws IOException{
		serverSocket = ServerSocketChannel.open();
		listenerSelector = Selector.open();
		serverSocket.bind(new InetSocketAddress(ip, port));
		serverSocket.configureBlocking(false);
		serverSocket.register(listenerSelector, SelectionKey.OP_ACCEPT);
		connRegistrarQ = new LinkedBlockingQueue<SocketChannel>();
		listener = new Listener(listenerSelector,connRegistrarQ);
		reader = new Reader(); // TODO : configure the number of reader Threads
		connRegistrar = new ConnectionRegistrar(connRegistrarQ, reader.getSelector());
		
	}
	public void start() throws IOException{
		initilaize();
		// Start all the Reader, Responder and ConnectRegistratThreads;
		System.out.println("Starting server on port :" + port);
		listener.start();
		reader.start();
		connRegistrar.start();
	}
}
