package com.soul.dsipc.server.protocol.impl;

import com.google.protobuf.RpcController;
import com.google.protobuf.ServiceException;
import com.soul.dsipc.server.protocol.DataNodeRegistration;
import com.soul.hdfs.datanode.proto.DatanodeProtocolProtos.RegisterDatanodeRequestProto;
import com.soul.hdfs.datanode.proto.DatanodeProtocolProtos.RegisterDatanodeResponseProto;

public class DataNodeProtocolImpl implements DataNodePB {

	public RegisterDatanodeResponseProto registerDatanode(RpcController controller,
			RegisterDatanodeRequestProto request) throws ServiceException {
		// TODO Auto-generated method stub
		
		DataNodeRegistration reg = PBHelper.convert(request.getRegistration());
		
		System.out.println("Method invoked");
		
		return RegisterDatanodeResponseProto.newBuilder()
        .setRegistration(PBHelper.convert(reg)).build();
	}
	
}
