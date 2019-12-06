package com.soul.dsipc.rpc;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.google.protobuf.BlockingService;
import com.google.protobuf.Descriptors.MethodDescriptor;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.ServiceException;
import com.soul.dsipc.rpc.packet.RpcRequestWrapper;
import com.soul.dsipc.server.EverRunningComponent;
import com.soul.dsipc.server.RpcCall;
import com.soul.hadoop.common.protobuf.ProtobufRpcEngineProtos.RequestHeaderProto;

/*
 * This class will receive the Rpc Packets from the Packet Processor
 * The router will send the Packet to the respective Rpc Services 
 */
public class RpcServiceImplRouter  extends Thread implements EverRunningComponent{

	Map<String,Object> implMap;
	BlockingQueue<RpcCall> queue = new LinkedBlockingQueue<RpcCall>(64); //TODO : make size configurable
	
	public BlockingQueue<RpcCall> getQueue() {
		return queue;
	}

	public RpcServiceImplRouter(){
		this.implMap = new HashMap<String, Object>();
		//this.isRunning = true;
	}
	
	public void registerRpcService(String rpcName, Object rpcSvc){
		this.implMap.put(rpcName, rpcSvc);
	}

	public void run() {
		System.out.println("==========RpcServiceImplRouter==============");
		while(true){  // TODO : Replace with isRunning
			
			try {
				RpcCall call = queue.take();
				RequestHeaderProto header = call.getReq().getMessageHeader();
				String methodName = header.getMethodName();
				String protoName = header.getDeclaringClassProtocolName();
				BlockingService svc = (BlockingService)implMap.get(protoName);
				MethodDescriptor methodDescriptor = svc.getDescriptorForType().findMethodByName(methodName);
				
				Message prototype = svc.getRequestPrototype(methodDescriptor);
		        try {
					Message param = prototype.newBuilderForType()
					    .mergeFrom(call.getReq().getMessageBytes()).build();
					Message result = svc.callBlockingMethod(methodDescriptor, null, param);
					
				} catch (InvalidProtocolBufferException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ServiceException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}
