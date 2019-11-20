package com.soul.dsipc.rpc.packet;

import java.io.IOException;

import com.google.protobuf.Message;
import com.soul.hadoop.common.protobuf.ProtobufRpcEngineProtos.RequestHeaderProto;

public class RpcRequestWrapper extends RpcMessageWithHeader<RequestHeaderProto> {

	
	public RpcRequestWrapper() {}
    
    public RpcRequestWrapper(
        RequestHeaderProto requestHeader, Message theRequest) {
      super(requestHeader, theRequest);
    }
    
    @Override
    RequestHeaderProto parseHeaderFrom(byte[] bytes) throws IOException {
      return RequestHeaderProto.parseFrom(bytes);
    }
    
    @Override
    public String toString() {
      return requestHeader.getDeclaringClassProtocolName() + "." +
          requestHeader.getMethodName();
    }
	
}
