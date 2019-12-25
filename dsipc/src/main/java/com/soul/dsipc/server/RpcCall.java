package com.soul.dsipc.server;

import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.SocketChannel;

import com.google.protobuf.Message;
import com.soul.dsipc.rpc.packet.RpcRequestWrapper;

public class RpcCall {

	private RpcRequestWrapper req;
	private SocketChannel conn;
	private ByteBuffer resp;
	private Message severResponse; // Server Response Object  
	
	public Message getSeverResponse() {
		return severResponse;
	}

	public void setSeverResponse(Message severResponse) {
		this.severResponse = severResponse;
	}

	public RpcCall(RpcRequestWrapper req, SocketChannel conn){
		this.req = req;
		this.conn = conn;
	}
	
	public RpcRequestWrapper getReq() {
		return req;
	}
	public SocketChannel getConn() {
		return conn;
	}
	public ByteBuffer getResp() {
		return resp;
	}
	
	
}
