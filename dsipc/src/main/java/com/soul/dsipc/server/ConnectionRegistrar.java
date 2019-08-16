package com.soul.dsipc.server;

import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.BlockingQueue;

public class ConnectionRegistrar extends Thread implements EverRunningComponent {

	private BlockingQueue<SocketChannel> connRegistrarQ;
	private Selector readSelector;
	public ConnectionRegistrar(BlockingQueue<SocketChannel> connRegistrarQ, Selector readSelector){
		this.connRegistrarQ = connRegistrarQ;
		this.readSelector = readSelector;
	}
	
	public void run(){
		System.out.println("ConnnectionRegistrar Started");
		while(isRunning){
			try {
				SocketChannel channel = connRegistrarQ.take();
				readSelector.wakeup();
				channel.register(readSelector, SelectionKey.OP_READ);
				System.out.println("Registered new connection of READ");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClosedChannelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public boolean isRunning() {
		// TODO Auto-generated method stub
		return false;
	}
}
