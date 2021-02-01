package com.zc.aio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * @ClassName AIOClient
 * @Author 周聪
 * @Date 2021/2/1 23:39
 * @Version 1.0
 * @Description AIO客户端
 */
public class AIOClient {

    private final AsynchronousSocketChannel clientChannel;

    public AIOClient() throws Exception {
        clientChannel = AsynchronousSocketChannel.open();
    }

    public static void main(String[] args) throws Exception {
        new AIOClient().connect("localhost", 8000);
    }

    public void connect(String host, int port) throws Exception {
        clientChannel.connect(new InetSocketAddress(host, port), null, new CompletionHandler<Void, Object>() {
            /**
             * 回调成功状态的方法
             * @param result
             * @param attachment
             */
            @Override
            public void completed(Void result, Object attachment) {
                try {
                    clientChannel.write(ByteBuffer.wrap("这是一条测试数据".getBytes())).get();
                    System.out.println("已发送至服务器");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            /**
             * 回调失败状态的方法
             * @param exc
             * @param attachment
             */
            @Override
            public void failed(Throwable exc, Object attachment) {
                exc.printStackTrace();
            }
        });

        final ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        clientChannel.read(byteBuffer, null, new CompletionHandler<Integer, Object>() {
            /**
             * 回调成功状态的方法
             * @param result
             * @param attachment
             */
            @Override
            public void completed(Integer result, Object attachment) {
                System.out.println("IO操作完成:" + result);
                System.out.println("获取反馈结果:" + new String(byteBuffer.array()));
            }

            /**
             * 回调失败状态的方法
             * @param exc
             * @param attachment
             */
            @Override
            public void failed(Throwable exc, Object attachment) {
                exc.printStackTrace();
            }
        });

        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
