package com.soul.dsipc.rpc;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.concurrent.BlockingQueue;

import com.google.protobuf.CodedOutputStream;
import com.google.protobuf.Message;
import com.soul.dsipc.rpc.packet.RpcResponseWrapper;
import com.soul.dsipc.server.RpcCall;
import com.soul.hadoop.common.protobuf.RpcHeaderProtos.RpcResponseHeaderProto;
import com.soul.hadoop.common.protobuf.RpcHeaderProtos.RpcResponseHeaderProto.RpcStatusProto;

public class ResponseWriter extends Thread {

	/**
	 * When the read or write buffer size is larger than this limit, i/o will be
	 * done in chunks of this size. Most RPC requests and responses would be be
	 * smaller.
	 */
	private static int NIO_BUFFER_LIMIT = 8 * 1024; // should not be more than
													// 64KB.
	
	/**
	   * Initial and max size of response buffer
	   */
	  static int INITIAL_RESP_BUF_SIZE = 10240;

	private BlockingQueue<RpcCall> respQ;

	public ResponseWriter(BlockingQueue<RpcCall> respQ) {
		this.respQ = respQ;
	}

	public void run() {

		while (true) { // TODO : Add isRunning flag

			try {
				RpcCall call = respQ.take();

				Message resp = call.getSeverResponse();
				if (resp != null) {

					ByteArrayOutputStream buf = new ByteArrayOutputStream(10240); // TODO
																					// :
																					// make
																					// this
																					// configurable

					// Preparing the Response
					DataOutputStream out = new DataOutputStream(buf);
					RpcResponseHeaderProto.Builder headerBuilder = RpcResponseHeaderProto.newBuilder().setCallId(1)
							.setStatus(RpcStatusProto.SUCCESS);

					RpcResponseHeaderProto header = headerBuilder.build();
					final int headerLen = header.getSerializedSize();
					int fullLength = CodedOutputStream.computeRawVarint32Size(headerLen) + headerLen;

					RpcResponseWrapper respWrapper = new RpcResponseWrapper(resp);

					fullLength += respWrapper.getLength();
					try {
						// Writing response over wire
						out.writeInt(fullLength);
						header.writeDelimitedTo(out);
						respWrapper.write(out);
						channelWrite(call.getConn(), ByteBuffer.wrap(buf.toByteArray()));
						System.out.println("Response sent to client");
					} catch (IOException e) {
						// TODO send error response
						e.printStackTrace();
					}

				}

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private int channelWrite(WritableByteChannel channel, ByteBuffer buffer) throws IOException {

		int count = (buffer.remaining() <= NIO_BUFFER_LIMIT) ? channel.write(buffer) : channelIO(null, channel, buffer);
		return count;
	}

	private static int channelIO(ReadableByteChannel readCh, WritableByteChannel writeCh, ByteBuffer buf)
			throws IOException {

		int originalLimit = buf.limit();
		int initialRemaining = buf.remaining();
		int ret = 0;

		while (buf.remaining() > 0) {
			try {
				int ioSize = Math.min(buf.remaining(), NIO_BUFFER_LIMIT);
				buf.limit(buf.position() + ioSize);

				ret = (readCh == null) ? writeCh.write(buf) : readCh.read(buf);

				if (ret < ioSize) {
					break;
				}

			} finally {
				buf.limit(originalLimit);
			}
		}

		int nBytes = initialRemaining - buf.remaining();
		return (nBytes > 0) ? nBytes : ret;
	}

}
