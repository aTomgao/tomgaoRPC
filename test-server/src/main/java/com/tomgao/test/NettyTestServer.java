package com.tomgao.test;

import com.tomgao.rpc.annotation.ServiceScan;
import com.tomgao.rpc.api.HelloService;
import com.tomgao.rpc.serializer.CommonSerializer;
import com.tomgao.rpc.transport.RpcServer;
import com.tomgao.rpc.transport.netty.server.NettyServer;

@ServiceScan
public class NettyTestServer {


    public static void main(String[] args) {
//        HelloService helloService = new HelloServiceImpl();
//        NettyServer server = new NettyServer("127.0.0.1", 9999, CommonSerializer.PROTOBUF_SERIALIZER);
//        server.publishService(helloService, HelloService.class);
        RpcServer server = new NettyServer("127.0.0.1", 9999, CommonSerializer.PROTOBUF_SERIALIZER);
        server.start();
    }
}
