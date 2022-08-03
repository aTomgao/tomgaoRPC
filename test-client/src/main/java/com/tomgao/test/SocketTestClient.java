package com.tomgao.test;

import com.tomgao.rpc.RpcClientProxy;
import com.tomgao.rpc.api.HelloObject;
import com.tomgao.rpc.api.HelloService;
import com.tomgao.rpc.serializer.KryoSerializer;
import com.tomgao.rpc.socket.client.SocketClient;

import java.net.Socket;

public class SocketTestClient {

    public static void main(String[] args) {
        SocketClient client = new SocketClient("127.0.0.1", 9000);
        client.setSerializer(new KryoSerializer());
        RpcClientProxy proxy = new RpcClientProxy(client);
        HelloService helloService = proxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(12, "this is a message");
        String res = helloService.hello(object);
        System.out.println(res);
    }
}
