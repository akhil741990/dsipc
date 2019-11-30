package com.soul.dsipc.rpc;

import java.io.DataInput;
import java.io.IOException;

import com.google.protobuf.ByteString;
import com.soul.hadoop.common.protobuf.RpcHeaderProtos.RpcRequestHeaderProto;

public class Utils {
	
	public static int readRawVarint32(DataInput in) throws IOException {
	    byte tmp = in.readByte();
	    if (tmp >= 0) {
	      return tmp;
	    }
	    int result = tmp & 0x7f;
	    if ((tmp = in.readByte()) >= 0) {
	      result |= tmp << 7;
	    } else {
	      result |= (tmp & 0x7f) << 7;
	      if ((tmp = in.readByte()) >= 0) {
	        result |= tmp << 14;
	      } else {
	        result |= (tmp & 0x7f) << 14;
	        if ((tmp = in.readByte()) >= 0) {
	          result |= tmp << 21;
	        } else {
	          result |= (tmp & 0x7f) << 21;
	          result |= (tmp = in.readByte()) << 28;
	          if (tmp < 0) {
	            // Discard upper 32 bits.
	            for (int i = 0; i < 5; i++) {
	              if (in.readByte() >= 0) {
	                return result;
	              }
	            }
	            throw new IOException("Malformed varint");
	          }
	        }
	      }
	    }
	    return result;
	  }

	
	 public static RpcRequestHeaderProto makeRpcRequestHeader(RpcRequestHeaderProto.OperationProto operation) {
		    RpcRequestHeaderProto.Builder result = RpcRequestHeaderProto.newBuilder();
		    result.setRpcOp(operation).setCallId(1)
		        .setClientId(ByteString.copyFrom("uuid".getBytes()));
		    return result.build();
		  }
}
