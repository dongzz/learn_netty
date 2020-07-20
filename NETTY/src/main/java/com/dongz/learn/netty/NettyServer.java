package com.dongz.learn.netty;


import com.dongz.learn.netty.handler.HelloHandler;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description 服务端
 * @Auther dz <895180729@qq.com>
 * @Version V1.0.0
 * @Since 1.8
 * @Date 2020/7/20 11:06
 */
public class NettyServer {
    public static void main(String[] args) {
        // 服务类
        ServerBootstrap bootstrap = new ServerBootstrap();

        // 线程池
        ExecutorService boss = Executors.newCachedThreadPool();
        ExecutorService worker = Executors.newCachedThreadPool();

        // 设置niosocket工厂
        bootstrap.setFactory(new NioServerSocketChannelFactory(boss, worker));
        // 设置管道工厂
        bootstrap.setPipelineFactory(() -> {
            ChannelPipeline pipeline = Channels.pipeline();
            pipeline.addLast("helloHandler", new HelloHandler());
            return null;
        });

        // 绑定端口
        bootstrap.bind(new InetSocketAddress(8080));
    }
}
