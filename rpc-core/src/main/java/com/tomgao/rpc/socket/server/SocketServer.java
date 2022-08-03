package com.tomgao.rpc.socket.server;

import com.tomgao.rpc.RequestHandler;
import com.tomgao.rpc.RpcServer;
import com.tomgao.rpc.enumeration.RpcError;
import com.tomgao.rpc.exception.RpcException;
import com.tomgao.rpc.registry.ServiceRegistry;
import com.tomgao.rpc.serializer.CommonSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

public class SocketServer implements RpcServer {

    private static final Logger logger = LoggerFactory.getLogger(SocketServer.class);
    private static final int CORE_POOL_SIZE = 5;
    private static final int MAXIMUM_POOL_SIZE = 50;
    private static final int KEEP_ALIVE_TIME = 60;
    private static final int BLOCKING_QUEUE_CAPACITY = 100;

    private CommonSerializer serializer;

    private final ExecutorService threadPool;
    private RequestHandler requestHandler = new RequestHandler();
    private final ServiceRegistry serviceRegistry;
    BlockingQueue<Runnable> workingQueue = new ArrayBlockingQueue<>(100);
    ThreadFactory threadFactory = Executors.defaultThreadFactory();

    public SocketServer(ServiceRegistry serviceRegistry) {
        this.serviceRegistry = serviceRegistry;
        BlockingQueue<Runnable> workingQueue = new ArrayBlockingQueue<>(BLOCKING_QUEUE_CAPACITY);
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        threadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS, workingQueue, threadFactory);
    }

    @Override
    public void start(int port) {
        if (serializer == null) {
            logger.error("未设置序列化器");
            throw new RpcException(RpcError.UNKNOWN_SERIALIZER);
        }
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            logger.info("服务器启动");
            Socket socket;
            while ((socket = serverSocket.accept()) != null) {
                logger.info("消费者连接IP: {} : {}", socket.getInetAddress(), socket.getPort());
                threadPool.execute(new RequestHandlerThread(socket, requestHandler, serviceRegistry, serializer));
            }
            threadPool.shutdown();
        } catch (IOException e) {
            logger.error("连接时有错误发生..", e);
        }
    }

    @Override
    public void setSerializer(CommonSerializer serializer) {
        this.serializer =serializer;
    }

}
