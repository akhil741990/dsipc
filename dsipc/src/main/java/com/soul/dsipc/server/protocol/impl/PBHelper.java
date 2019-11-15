package com.soul.dsipc.server.protocol.impl;

import com.soul.dsipc.server.protocol.DataNodeRegistration;
import com.soul.hdfs.datanode.proto.DatanodeProtocolProtos.DatanodeRegistrationProto;

/*
 * This class will convert ProtoBuf classes into ServerImpl classes
 */
public class PBHelper {
	
	public static DataNodeRegistration convert(DatanodeRegistrationProto request){
		
		return new DataNodeRegistration(request.getDatanodeID(), 
				request.getStorageInfo(), request.getKeys(), request.getSoftwareVersion());
	}

	
	 public static DatanodeRegistrationProto convert(
			 DataNodeRegistration registration) {
		    DatanodeRegistrationProto.Builder builder = DatanodeRegistrationProto
		        .newBuilder();
		    return builder.setDatanodeID(registration.getDatanodeID())
		        .setStorageInfo(registration.getStorageInfo())
		        .setKeys(registration.getKeys())
		        .setSoftwareVersion(registration.getSoftwareVersion()).build();
		  }
}
