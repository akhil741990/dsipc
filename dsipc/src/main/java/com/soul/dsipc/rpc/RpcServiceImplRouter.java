package com.soul.dsipc.rpc;

import java.util.HashMap;
import java.util.Map;

/*
 * This class will receive the Rpc Packets from the Packet Processor
 * The router will send the Packet to the respective Rpc Services 
 */
public class RpcServiceImplRouter {

	Map<String,RpcService> implMap;
	
	public RpcServiceImplRouter(){
		this.implMap = new HashMap<String, RpcService>();
	}
	
	public void registerRpcService(String rpcName, RpcService rpcSvc){
		this.implMap.put(rpcName, rpcSvc);
	}
}
