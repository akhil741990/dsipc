package com.soul.dsipc;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.sql.Time;

public class Client {
	public static void main(String[] args) throws IOException, InterruptedException {
		SocketChannel client = SocketChannel.open(new InetSocketAddress("localhost", 5555));
		ByteBuffer buffer = ByteBuffer.allocate(256);
		ByteBuffer buff =ByteBuffer.wrap("hello".getBytes()); 
		client.write(buff);
		buff.flip();
		buff.clear();
		Thread.sleep(5000);
		
		
	}
}
