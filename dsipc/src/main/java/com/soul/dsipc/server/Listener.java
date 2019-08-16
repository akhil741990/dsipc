package com.soul.dsipc.server;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

public class Listener extends Thread implements EverRunningComponent{

	private Selector selector;
	private BlockingQueue<SocketChannel> connRegistrarQ;
	public Listener(Selector selector, BlockingQueue<SocketChannel> connRegistrarQ){
		this.selector = selector;
		this.connRegistrarQ = connRegistrarQ;
	}
	
	public void run(){
		
		System.out.println("Waiting for incomming connection");
		while(isRunning){
			
			try {
				selector.select();
				Set<SelectionKey> keys =  selector.selectedKeys();
				Iterator<SelectionKey> itr = keys.iterator();
				while(itr.hasNext()){
					SelectionKey key  = itr.next();
					if(key.isValid() && key.isAcceptable()){
						// Add the channel to ConnectionRegistrarQueue;
						ServerSocketChannel server = (ServerSocketChannel)key.channel();
						SocketChannel client;
						while((client=server.accept()) != null){
							client.configureBlocking(false);
							connRegistrarQ.add(client);
							System.out.println("New client connection accepted");
						}
					}
					itr.remove();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
