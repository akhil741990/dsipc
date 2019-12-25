package com.soul.dsipc.rpc.packet;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.OutputStream;

import com.google.protobuf.CodedOutputStream;
import com.google.protobuf.Message;
import com.soul.dsipc.rpc.Utils;

public class RpcResponseWrapper implements RpcWrapper{

	Message theResponse; // for senderSide, the response is here
	byte[] theResponseRead; // for receiver side, the response is here
	
	public RpcResponseWrapper() {
    }

    public RpcResponseWrapper(Message message) {
      this.theResponse = message;
    }
	
	public void write(DataOutput out) throws IOException {
		OutputStream os = DataOutputOutputStream.constructOutputStream(out);
		theResponse.writeDelimitedTo(os);
		
	}

	public void readFields(DataInput in) throws IOException {
		int length = Utils.readRawVarint32(in);
	    theResponseRead = new byte[length];
	    in.readFully(theResponseRead);
	}

	public int getLength() {
		int resLen;
	      if (theResponse != null) {
	        resLen = theResponse.getSerializedSize();
	      } else if (theResponseRead != null ) {
	        resLen = theResponseRead.length;
	      } else {
	        throw new IllegalArgumentException(
	            "getLength on uninitialized RpcWrapper");      
	      }
	      return CodedOutputStream.computeRawVarint32Size(resLen) + resLen;
	}

}
