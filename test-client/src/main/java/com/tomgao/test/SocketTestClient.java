package com.tomgao.test;

import com.tomgao.rpc.api.ByeService;
import com.tomgao.rpc.api.HelloObject;
import com.tomgao.rpc.api.HelloService;
import com.tomgao.rpc.serializer.CommonSerializer;
import com.tomgao.rpc.transport.RpcClientProxy;
import com.tomgao.rpc.transport.socket.client.SocketClient;

public class SocketTestClient {

    public static void main(String[] args) {
        SocketClient client = new SocketClient(CommonSerializer.KRYO_SERIALIZER);
        RpcClientProxy proxy = new RpcClientProxy(client);
        HelloService helloService = proxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(12, "this is a message");
        String res = helloService.hello(object);
        System.out.println(res);
        ByeService byeService = proxy.getProxy(ByeService.class);
        System.out.println(byeService.bye("Netty"));
    }
}
