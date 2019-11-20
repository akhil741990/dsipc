package com.soul.dsipc.rpc.packet;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.OutputStream;

import com.google.protobuf.CodedOutputStream;
import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.Message;
import com.soul.dsipc.rpc.Utils;

public  abstract class RpcMessageWithHeader<T extends GeneratedMessage> implements RpcWrapper{
	
	T requestHeader;
	Message request; // for clientSide, the request is here
	byte[] requestRead; // for server side, the request is here
	
	
	public RpcMessageWithHeader(){
	}
	
	public RpcMessageWithHeader(T header, Message request){
		this.requestHeader = header;
		this.request = request;
	}
	
    public void write(DataOutput out) throws IOException {
      OutputStream os = DataOutputOutputStream.constructOutputStream(out);
      
      ((Message)requestHeader).writeDelimitedTo(os);
      request.writeDelimitedTo(os);
    }

    public void readFields(DataInput in) throws IOException {
      requestHeader = parseHeaderFrom(readVarintBytes(in));
      requestRead = readMessageRequest(in);
    }

    abstract T parseHeaderFrom(byte[] bytes) throws IOException;

    byte[] readMessageRequest(DataInput in) throws IOException {
      return readVarintBytes(in);
    }

    private static byte[] readVarintBytes(DataInput in) throws IOException {
      final int length = Utils.readRawVarint32(in);
      final byte[] bytes = new byte[length];
      in.readFully(bytes);
      return bytes;
    }

    public T getMessageHeader() {
      return requestHeader;
    }

    public byte[] getMessageBytes() {
      return requestRead;
    }
    
    public int getLength() {
      int headerLen = requestHeader.getSerializedSize();
      int reqLen;
      if (request != null) {
        reqLen = request.getSerializedSize();
      } else if (requestRead != null ) {
        reqLen = requestRead.length;
      } else {
        throw new IllegalArgumentException(
            "getLength on uninitialized RpcWrapper");      
      }
      return CodedOutputStream.computeRawVarint32Size(headerLen) +  headerLen
          + CodedOutputStream.computeRawVarint32Size(reqLen) + reqLen;
    }
 
}
