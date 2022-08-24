package com.ljp.client;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.log4j.Logger;

public class SimpleChatClientHandler extends  SimpleChannelInboundHandler<String> {
    Logger logger = Logger.getLogger(SimpleChatClientHandler.class);
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String s) throws Exception {
        logger.info(s);
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (7)
        Channel channel = ctx.channel();
        logger.error("SimpleChatServer:"+channel.remoteAddress() +"异常, channel的id是：" + channel.id());
        // 当出现异常就关闭连接
        cause.printStackTrace();
        ctx.close();
    }
}