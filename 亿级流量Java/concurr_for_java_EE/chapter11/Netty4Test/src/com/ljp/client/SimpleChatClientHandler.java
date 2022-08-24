package com.ljp.client;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class SimpleChatClientHandler extends  SimpleChannelInboundHandler<String> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String s) throws Exception {
        System.out.println(s);
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (7)
        Channel channel = ctx.channel();
        System.out.println("SimpleChatServer:"+channel.remoteAddress()
                +"异常, channel的id是：" + channel.id());
        // 当出现异常就关闭连接
        cause.printStackTrace();
        ctx.close();
    }
}