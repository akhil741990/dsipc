package com.soul.dsipc.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.soul.dsipc.rpc.RpcService;
import com.soul.dsipc.rpc.RpcServiceImplRouter;
import com.soul.dsipc.server.protocol.impl.DataNodePB;
import com.soul.dsipc.server.protocol.impl.DataNodeProtocolImpl;
import com.soul.hdfs.datanode.proto.DatanodeProtocolProtos;
import com.soul.hdfs.datanode.proto.DatanodeProtocolProtos.DatanodeProtocolService;

public class RpcServer {

	private String ip;
	private int port;
	private ServerSocketChannel serverSocket;
	private Selector listenerSelector;
	private Listener listener;
	private Reader reader;
	private ConnectionRegistrar connRegistrar;
	private BlockingQueue<SocketChannel> connRegistrarQ;
	private RpcServiceImplRouter svcRouter;
	
	private static RpcServer instance;
	
	public static RpcServer  getServerInstance(String ip, int port){
		
		if(instance == null){
			instance = new RpcServer(ip, port);
		}
		return instance;
		
	}
	
	private RpcServer(String ip, int port){
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
		svcRouter = new RpcServiceImplRouter();
		DataNodeProtocolImpl dataNodeSvc = new DataNodeProtocolImpl();
		
		svcRouter.registerRpcService(DataNodePB.class.getSimpleName(),
				DatanodeProtocolService.newReflectiveBlockingService(dataNodeSvc));
		
	}
	public void start() throws IOException{
		initilaize();
		// Start all the Reader, Responder and ConnectRegistratThreads;
		System.out.println("Starting server on port :" + port);
		listener.start();
		reader.start();
		connRegistrar.start();
		svcRouter.start();
		
	}
	
	public void registerRpc(Class c , RpcService svc){
		svcRouter.registerRpcService(c.getName(), svc);
	}
	public  BlockingQueue<RpcCall> getQueue(){
		return this.svcRouter.getQueue();
	}
}
