

                         Google Protobuf based RPC Server Implementation


RPC Server :
    RPC server run a TCP server to listen to the in-comming connection. The TCP server is built using Java NIO selector.
Listener : 
It accepts the incoming connection and registers the client socket channel 
with the ‘read’ selector  and  adds an attachment (ie ConnectionHandler) .
Please refer (https://docs.oracle.com/javase/7/docs/api/java/nio/channels/SelectionKey.html)
for  Java NIO Selector.
Reader: The read selector will be used by Reader to iterate over all the incoming client network traffic. This ConnectionHandler will be used for processing the subsequent network bytes that arrives via the client socket. 

ConnectionHandler  :  It will parse the network bytes to build the RPC request, 

    DataLength : First 4 bytes  
		Data : 
			Next dataLength Bytes

Data contains the RpcHeader and RpcBody.
RpcHeader : Its contains the protocolName, RPC method name, etc 
RpcBody : Its contains the RpcStub object and the params for the method

From the Protocol Map we get the ProtocolImplementation via a lookup based on protocolName. Using Protobuf APIs we invoke the respective method of the ProtocolImplementation and send the response back to client.

RPC Client : 
Client use Stub to invoke the method Remote RPC server.  Here the stub is Protocol class. Wiki link for stub (https://en.wikipedia.org/wiki/Stub_(distributed_computing))
In client we build a ProtocolProxy using the Stub class. 
ProtocolProxy contains the following :
a) TCP  client to send the RPC Request to the Server
b) Invoker : This is an implementation of Java Invocation Handler (https://docs.oracle.com/javase/7/docs/api/java/lang/reflect/InvocationHandler.html)

Following is the code snippet to build the ProtocolProxy :
/**
proxy = RPC.getProtocolProxy(
TestProtocol0.class, TestProtocol0.versionID, serverIP, conf);
                        TestProtocol0 proxy0 = (TestProtocol0)proxy.getProxy();
 proxy0.ping();
**/

Here, TestProtocol0 is the Protocol Class / Stub
          when  the client class a method in the Proxy object i.e is “ping” method.
          The invoke method of Invoker is called . Following is the signature of invoke method : 
public Object invoke(Object proxy, Method method, Object[] args) 
Inside the invoke method , proxy, method called on the proxy and its parameter are wrapped into a RPC Request and is sent to the RPC Server



This project is inspired by apache/hadoop-common github project
