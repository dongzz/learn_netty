package com.dongz.learn.netty4.client.handler;

import com.dongz.learn.netty4.RPCParam;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.Callable;

/**
 * @Description
 * @Auther dz <895180729@qq.com>
 * @Version V1.0.0
 * @Since 1.8
 * @Date 2020/7/20 15:59
 */
public class RPCClientHandler extends ChannelInboundHandlerAdapter implements Callable {
    private ChannelHandlerContext context;
    //结果
    private Object result;
    //参数
    private RPCParam rpcParam;

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        context = ctx;
    }

    /**
     * 收到服务端数据，唤醒等待线程
     */
    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) {
        result = msg;
        notify();
    }

    /**
     * 写出数据，开始等待唤醒
     */
    @Override
    public synchronized Object call() throws InterruptedException {

        context.writeAndFlush(rpcParam);
        //context.writeAndFlush("as");
        wait();
        return result;
    }

    public RPCParam getRpcParam() {
        return rpcParam;
    }

    public void setRpcParam(RPCParam rpcParam) {
        this.rpcParam = rpcParam;
    }
}
