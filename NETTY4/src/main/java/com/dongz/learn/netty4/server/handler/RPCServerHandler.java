package com.dongz.learn.netty4.server.handler;

import com.dongz.learn.netty4.RPCParam;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.EventExecutorGroup;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Description
 * @Auther dz <895180729@qq.com>
 * @Version V1.0.0
 * @Since 1.8
 * @Date 2020/7/20 16:09
 */
public class RPCServerHandler  extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        System.out.println(msg.toString());
        RPCParam rpcParam = (RPCParam) msg;
        String className =  rpcParam.getClazzName();
        String methodName = rpcParam.getMethodName();

        try {
            Class clazz = Class.forName(className);
            Object object = clazz.newInstance();

            Method method = object.getClass().getDeclaredMethod(methodName,rpcParam.getParamTypes());

            Object result = method.invoke(object,rpcParam.getParams());
            ctx.writeAndFlush(result);
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }

    }
}