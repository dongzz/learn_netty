package com.dongz.learn.netty4.client;

import com.dongz.learn.netty4.RPCParam;
import com.dongz.learn.netty4.client.handler.RPCClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.lang.reflect.Proxy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description RPC 客户端
 * @Auther dz <895180729@qq.com>
 * @Version V1.0.0
 * @Since 1.8
 * @Date 2020/7/20 15:54
 */
public class RPCClient {
    private static ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private static RPCClientHandler client;


    /**
     * 创建一个代理对象
     */
    public Object createProxy(final Class<?> serviceClass, final String providerName
            , final RPCClientHandler rpcClientHandler, String ip, int port) {
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class<?>[]{serviceClass}, (proxy, method, args) -> {
                    if (client == null) {
                        initClient(rpcClientHandler, ip, port);
                    }


                    String param = providerName;
                    String[] params = param.split("#");

                    String className = params[0];
                    String methodName = params[1];
                    RPCParam rpcParam =new RPCParam();
                    rpcParam.setClazzName(className);
                    rpcParam.setMethodName(methodName);
                    rpcParam.initParams(args.length);
                    for (int i = 0; i < args.length; i++) {
                        rpcParam.getParams()[i]=args[i];
                        rpcParam.getParamTypes()[i] = args[i].getClass();
                    }
                    // 设置参数
                    client.setRpcParam(rpcParam);
                    return executor.submit(client).get();
                });
    }

    /**
     * 初始化客户端
     */
    private static void initClient(RPCClientHandler rpcClientHandler, String ip, int port) {
        client = rpcClientHandler;
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline p = ch.pipeline();
                        p.addLast(new ObjectEncoder());
                        p.addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));
                        p.addLast(client);
                    }
                });
        try {
            b.connect(ip, port).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
