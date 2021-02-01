package com.zc.bio;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.UUID;

/**
 * @ClassName BIOClient
 * @Author 周聪
 * @Date 2021/2/1 21:43
 * @Version 1.0
 * @Description BIO和NIO的客户端
 */
public class BIOClient {
//    FileOutputStream、FileInputStream 这里不拿磁盘操作案例，大家都很熟悉

    public static void main(String[] args) throws IOException {
//        要和谁进行通信，服务器IP、服务器的端口
//      一台机器的端口号是有限的
        Socket client = new Socket("localhost", 8080);
//        输出 不管是客户端还是服务端，都有可能write和read
        OutputStream os = client.getOutputStream();
//        生成一个随机的ID
        String name = UUID.randomUUID().toString();
        System.out.println("客户端发送数据：" + name);
        os.write(name.getBytes());
        os.close();
        client.close();
    }
}
