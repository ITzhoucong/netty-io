package com.zc.aio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @ClassName AIOServer
 * @Author 周聪
 * @Date 2021/2/1 23:21
 * @Version 1.0
 * @Description AIO服务端
 */
public class AIOServer {

    private final int port;

    public AIOServer(int port) {
        this.port = port;
        listen();
    }

    public static void main(String[] args) {
        int port = 8000;
        new AIOServer(port);
    }

    private void listen() {
        try {
            ExecutorService executorService = Executors.newCachedThreadPool();
            AsynchronousChannelGroup threadPool = AsynchronousChannelGroup.withCachedThreadPool(executorService, 1);
//            开门营业
//            工作线程,用来侦听回调,事件响应的时候需要回调
            final AsynchronousServerSocketChannel server = AsynchronousServerSocketChannel.open(threadPool);
            server.bind(new InetSocketAddress(port));
            System.out.println("服务已启动,监听端口: " + port);

//            准备接受数据
            server.accept(null, new CompletionHandler<AsynchronousSocketChannel, Object>() {

                final ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

                /**
                 * 回调有两个状态:成功
                 * 实现completed方法来回调 由操作系统来触发
                 * @param result
                 * @param attachment
                 */
                @Override
                public void completed(AsynchronousSocketChannel result, Object attachment) {
                    System.out.println("IO操作成功,开始获取数据");
                    try {
                        byteBuffer.clear();
                        result.read(byteBuffer).get();
                        byteBuffer.flip();
                        result.write(byteBuffer);
                        byteBuffer.flip();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            result.close();
                            server.accept(null, this);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("操作完成");
                }

                /**
                 * 回调有两个状态:失败
                 * @param exc
                 * @param attachment
                 */
                @Override
                public void failed(Throwable exc, Object attachment) {
                    System.out.println("IO操作失败: " + exc.getStackTrace());
                }

            });

            try {
                Thread.sleep(Integer.MAX_VALUE);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
