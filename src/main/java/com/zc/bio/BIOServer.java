package com.zc.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @ClassName BIOServer
 * @Author 周聪
 * @Date 2021/2/1 21:50
 * @Version 1.0
 * @Description BIO服务端
 */
public class BIOServer {

    /**
     * 服务的网络IO模型的封装对象
     */
    ServerSocket serverSocket;

    /**
     * 服务器
     *
     * @param port
     */
    public BIOServer(int port) {
        try {
//            Tomcat 默认端口8080
//            只要是Java写的底层都是ServerSocket
            serverSocket = new ServerSocket(port);
            System.out.println("BIO服务已启动，监听端口是：" + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        new BIOServer(8080).listen();
    }

    /**
     * 开始监听，并处理逻辑
     *
     * @throws IOException
     */
    public void listen() throws IOException {
//        循环监听
        while (true) {
//            等待客户端连接，阻塞方法,只有客户端把数据发过来的时候才会动，否则一直等,程序不会进行下去
//            Socket数据发送者在服务端的引用
            Socket client = serverSocket.accept();
            System.out.println(client.getPort());
//            对方发数据给我了，读Input
            InputStream is = client.getInputStream();
//            JVM内存
//            网络客户端把数据发送到网卡，机器所得到的数据读到了JVM内存中
            byte[] buff = new byte[1024];
            int len = is.read(buff);
            if (len > 0) {
                String msg = new String(buff, 0, len);
                System.out.println("收到" + msg);
            }

        }
    }
}
