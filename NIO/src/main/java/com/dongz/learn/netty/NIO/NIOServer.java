package com.dongz.learn.netty.NIO;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @Description NIO
 * @Auther dz <895180729@qq.com>
 * @Version V1.0.0
 * @Since 1.8
 * @Date 2020/7/19 20:45
 */
public class NIOServer {
    // 通道管理器
    private Selector selector;

    /**
     * 启动服务端测试
     */ 
    public static void main(String[] args) throws IOException {
        NIOServer testServer = new NIOServer();
        testServer.initServer(8080);
        testServer.listen();
    }
    

    /** 
     * 获取一个serverSocket通道，并对该通道做一些初始化的工作
     */
    public void initServer(int port) throws IOException {
        // 获得一个ServerScoket 通道
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        // 设置通道为非阻塞
        serverChannel.configureBlocking(false);
        // 将该通道对应的ServerSocket绑定到port端口
        serverChannel.socket().bind(new InetSocketAddress(port));
        // 获得一个通道管理器
        this.selector = Selector.open();
        // 将通道管理器和该通道绑定， 并为该通道注册SelectionKey.OP_ACCEPT事件，注册事件后
        // 当该事件到达时，selector.select() 会返回，如果该事件没到达select.select() 会一直阻塞
        serverChannel.register(this.selector, SelectionKey.OP_ACCEPT);
    }

    /**
     * 采用轮询的方式监听selector上是否有需要处理的事件，如果有，则进行处理
     */
    public void listen() throws IOException {
        System.out.println("服务端启动成功");
        // 轮询访问selector
        while(true) {
            // 当注册的事件到达时，方法返回， 否则， 该方法会一直阻塞
            this.selector.select();
            // 获得selector中选中的项的迭代器，选中的项为注册的事件
            Iterator<?> ite = this.selector.selectedKeys().iterator();
            while (ite.hasNext()) {
                SelectionKey key = (SelectionKey) ite.next();
                // 删除已选的key， 以防止重复处理
                ite.remove();

                handler(key);
            }
        }
    }
    /**
     * 处理请求
     */
    public void handler(SelectionKey key) throws IOException {
        //客户端请求连接事件
        if (key.isAcceptable()) {
            handlerAccept(key);
            // 获得可读的事件
        } else if (key.isReadable()){
            handlerRead(key);
        }
    }

    /**
     * 处理连接请求
     */
    public void handlerAccept(SelectionKey key) throws IOException {
        ServerSocketChannel server = (ServerSocketChannel) key.channel();
        // 获得和客户端连接的通道
        SocketChannel channel = server.accept();
        // 设置成非阻塞
        channel.configureBlocking(false);
        // 在这里可以给客户端发送消息
        System.out.println("新的客户端连接");
        // 在和客户端成功连接之后，为了可以接受到客户端的信息，需要给通道设置读的权限。
        channel.register(this.selector, SelectionKey.OP_READ);
    }

    /**
     * 处理读的事件
     */
    public void handlerRead(SelectionKey key) throws IOException {
        // 服务器可读取消息：得到事件发生的Socket通道
        SocketChannel channel = (SocketChannel) key.channel();
        // 创建读取的缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int read = channel.read(buffer);
        if (read > 0) {
            byte[] data = buffer.array();
            String msg = new String(data).trim();
            System.out.println("服务端收到信息：" + msg);
            ByteBuffer outBuffer = ByteBuffer.wrap("好的".getBytes());
            channel.write(outBuffer);// 将消息回给客户端
        } else {
            System.out.println("客户端关闭");
            key.cancel();
        }
    }
}
