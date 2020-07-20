package com.dongz.learn.netty.client;

import com.dongz.learn.netty.client.handler.HiHandler;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description 客户端
 * @Auther dz <895180729@qq.com>
 * @Version V1.0.0
 * @Since 1.8
 * @Date 2020/7/20 13:45
 */
public class NettyClient {
    public static void main(String[] args) {
        // 服务类
        ClientBootstrap bootstrap = new ClientBootstrap();
        // 线程池
        ExecutorService boss = Executors.newCachedThreadPool();
        ExecutorService worker = Executors.newCachedThreadPool();

        // socket工厂
        bootstrap.setFactory(new NioClientSocketChannelFactory(boss, worker));
        // 管道工厂
        bootstrap.setPipelineFactory(() -> {
            ChannelPipeline pipeline = Channels.pipeline();
            pipeline.addLast("decoder", new StringDecoder());
            pipeline.addLast("encoder", new StringEncoder());
            pipeline.addLast("hiHandler", new HiHandler());
            return pipeline;
        });

        // 连接服务端
        ChannelFuture connect = bootstrap.connect(new InetSocketAddress("127.0.0.1", 8080));
        Channel channel = connect.getChannel();
        System.out.println("client start");

        Scanner scanner = new Scanner(System.in);
        while(true) {
            System.out.println("client send message:");
            channel.write(scanner.nextLine());
        }
    }
}
