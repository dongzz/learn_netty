package com.dongz.learn.netty3.client.handler;

import org.jboss.netty.channel.*;

/**
 * @Description
 * @Auther dz <895180729@qq.com>
 * @Version V1.0.0
 * @Since 1.8
 * @Date 2020/7/20 13:49
 */
public class HiHandler extends SimpleChannelHandler {
    /**
     * 接收消息
     */
    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        System.out.println("messageReceived");
//        ChannelBuffer message = (ChannelBuffer) e.getMessage();
//        System.out.println(new String(message.array()));
        System.out.println(e.getMessage());

        super.messageReceived(ctx, e);
    }

    /**
     * 捕获异常
     * @param ctx
     * @param e
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        super.exceptionCaught(ctx, e);
    }

    /**
     * 新连接
     * @param ctx
     * @param e
     * @throws Exception
     */
    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        System.out.println("客户端连接");
        super.channelConnected(ctx, e);
    }

    /**
     * 断开连接
     * @param ctx
     * @param e
     * @throws Exception
     */
    @Override
    public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        super.channelDisconnected(ctx, e);
    }

    /**
     * 关闭客户端
     * @param ctx
     * @param e
     * @throws Exception
     */
    @Override
    public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        System.out.println("客户端已关闭");
        super.channelClosed(ctx, e);
    }
}
