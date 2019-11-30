package com.soul.dsipc;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.sql.Time;

import com.google.protobuf.ServiceException;
import com.soul.dsipc.rpc.RpcEngine;
import com.soul.dsipc.server.protocol.DataNodeRegistration;
import com.soul.dsipc.server.protocol.impl.DataNodePB;
import com.soul.dsipc.server.protocol.impl.PBHelper;
import com.soul.hdfs.datanode.proto.DatanodeProtocolProtos.RegisterDatanodeRequestProto;

public class Client {
	public static void main(String[] args) throws IOException, InterruptedException {
		SocketChannel client = SocketChannel.open(new InetSocketAddress("localhost", 5555));
		ByteBuffer buffer = ByteBuffer.allocate(256);
		ByteBuffer buff =ByteBuffer.wrap("hello".getBytes()); 
		client.write(buff);
		buff.flip();
		buff.clear();
		Thread.sleep(5000);
		
		
		DataNodePB proxy = (DataNodePB) RpcEngine.getProxy(DataNodePB.class, 1l,new InetSocketAddress("localhost", 5555));
		
	
		DataNodeRegistration dnReg = new DataNodeRegistration("dn", "storageInfo", "keys", "softwareVersion");
		
		 RegisterDatanodeRequestProto.Builder builder = RegisterDatanodeRequestProto
			        .newBuilder().setRegistration(PBHelper.convert(dnReg));
		
		
		 RegisterDatanodeRequestProto request = builder.build();
		
		try {
			proxy.registerDatanode(null, request);
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
