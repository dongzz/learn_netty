package com.dongz.learn.netty.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description bio 加线程池
 * @Auther dz <895180729@qq.com>
 * @Version V1.0.0
 * @Since 1.8
 * @Date 2020/7/19 20:33
 */
public class Test2Server {
    public static void main(String[] args) throws IOException {
        // 创建线程
        ExecutorService executorService = Executors.newCachedThreadPool();
        // 创建socket服务，监听 8080 端口
        ServerSocket server = new ServerSocket(8080);
        System.out.println("服务启动了！");
        while(true) {
            // 获取一个套接字（阻塞）
            final Socket socket = server.accept();
            System.out.println("来了一个新客户端！");
            // 业务处理
            executorService.execute(() -> {
                handler(socket);
            });
        }
    }

    public static void handler(Socket socket) {
        try {
            byte[] bytes = new byte[1024];
            InputStream inputStream = socket.getInputStream();

            while(true){
                // 读取数据（阻塞）
                int read = inputStream.read(bytes);
                if (read != -1){
                    System.out.println(new String(bytes, 0, read));
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                System.out.println("socket关闭");
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
