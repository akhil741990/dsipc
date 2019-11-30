package com.soul.dsipc.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import com.soul.dsipc.server.net.RpcConnectionHandler;

public class Reader extends Thread implements EverRunningComponent {


	private Selector selector;
	public Reader() throws IOException{
		this.selector  = Selector.open();
	}
	
	public Selector getSelector(){
		return selector;
	}
	
	public void run(){
		ByteBuffer buffer =  ByteBuffer.allocate(256);
		System.out.println("Reader started");
		while(isRunning){
			try {
				selector.select();
				Set<SelectionKey> keys =  selector.selectedKeys();
				Iterator<SelectionKey> itr = keys.iterator();
				while(itr.hasNext()){
					try{
						SelectionKey key = itr.next();
						SocketChannel client = (SocketChannel) key.channel();
						
						if(client.isConnected()){
							//TODO : attach an Object which has the capability to process the network Bytes
//							client.read(buffer);
//							System.out.println("Msg Recieved :" + new String(buffer.array()));
//							buffer.flip();
//							buffer.clear();
//							itr.remove();
							RpcConnectionHandler connHandler =   (RpcConnectionHandler) key.attachment();
							connHandler.readRpcRequest();
						}
					}catch (Exception e) {
						System.out.println("Error :" + e.getMessage());
						break;
					}
					
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
		}
	}
}

