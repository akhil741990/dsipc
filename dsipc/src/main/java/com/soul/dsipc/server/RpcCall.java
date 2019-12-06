package com.soul.dsipc.server;

import java.nio.ByteBuffer;
import java.nio.channels.Channel;

import com.soul.dsipc.rpc.packet.RpcRequestWrapper;

public class RpcCall {

	private RpcRequestWrapper req;
	private Channel conn;
	private ByteBuffer resp;
	
	public RpcCall(RpcRequestWrapper req, Channel conn){
		this.req = req;
		this.conn = conn;
	}
	
	public RpcRequestWrapper getReq() {
		return req;
	}
	public Channel getConn() {
		return conn;
	}
	public ByteBuffer getResp() {
		return resp;
	}
	
	
}
