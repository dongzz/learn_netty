# netty 学习与使用

# BIO
    单线程情况下只能有一个客户端
    用线程池可以有多个客户端连接， 但是非常消耗性能
-   test1 阻塞，线程等待
-   test2 用线程池，不阻塞，但是每连接一次就需要创建一个线程， 不能做长链接的服务器
# NIO
## 特点
    ServerSocketChannel --->    ServerScoket
    SocketChanner       --->    Socket
    Selector            
    SelectionKey
## 疑问
-   客户端关闭的时候会抛出异常，死循环

    解决方案：
    ```
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
    ```

-   selector.select();阻塞，那为什么说nio是非阻塞的io？
    ```
    1. nio是非阻塞io说的是read的时候，客户端未输入，服务器不会阻塞等待返回值
    2. selector.select() 可以不阻塞
       selector.select(100); 不阻塞， （100ms后返回）
       selector.wakeup(); 也可以唤醒selector
    ```

## 我们如何提高NIO的工作效率
### 一个NIO是不是只有一个selector？
    不是，一个系统可以有多个selector    
### selector是不是只能注册一个ServerSocketChannel
    不是，可以注册多个




# Netty
    SimpleChannelHandler        处理消息接收和写
    {
        messageReceived 接收消息
        channelConnected 新连接，通常用来检测IP是否是黑名单
        channelDisconnected 连接关闭，可以在用户断线的时候清除用户的缓存数据等
    }