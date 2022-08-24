package com.ljp.server;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.ljp.util.ReadPropertiesUtil;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import redis.clients.jedis.Jedis;

public class SimpleChatServerHandler extends SimpleChannelInboundHandler<String> { // (1)

    public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public static Map<String, Channel> mapChannelsSession = new HashMap<String, Channel>();

    
    Logger logger = Logger.getLogger(SimpleChatServerHandler.class);
    
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {  //覆盖了 handlerAdded() 事件处理方法。每当从服务端收到新的客户端连接时， 与客户端机器信息与channel一起保存到服务器的sessionMap中
        Channel incomingChannel = ctx.channel();
        String clientIpInfo = incomingChannel.remoteAddress().toString();
        
        channels.add(ctx.channel());
        mapChannelsSession.put(clientIpInfo, incomingChannel);
        logger.info("channel:" + incomingChannel.id() + "已加入");

    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {  // (3)
        Channel incomingChannel = ctx.channel();
        channels.remove(ctx.channel());
        logger.info("channel:" + incomingChannel.id() + "已移除");
        
    }
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String incomingString) throws Exception { // 覆盖了 channelRead0() 事件处理方法。每当从服务端读到客户端写入信息时，将信息转发给其他客户端的 channel
        Channel incomingChannel = ctx.channel();
 
        //记录更新应用与客户端机器信息列表对应关系到redis
        String clientIpInfo = incomingChannel.remoteAddress().toString();
        
        String[] redisIpAndPort = ReadPropertiesUtil.getPropertiesValue("systemConfig.properties", "redis").split(":");
    	Jedis redis = new Jedis(redisIpAndPort[0], Integer.parseInt(redisIpAndPort[1]));

        if(incomingString.length() > 5 && incomingString.substring(0, 5).equals("[reg]")){//如果是注册，则执行
        	String appId = incomingString.substring(5, incomingString.length());
        	redis.sadd(appId, clientIpInfo);
        	logger.info("有应用进行了注册操作：" + appId);
        }
        
        
        if(incomingString.length() > 6 && incomingString.substring(0, 6).equals("[push]")){//实际项目中，会以kafka-receiver来替代该段代码。
        	String appId = incomingString.substring(6, incomingString.lastIndexOf("["));
        	String userId = incomingString.substring(incomingString.lastIndexOf("[") + 1, incomingString.lastIndexOf("]"));
        	String message = incomingString.substring(incomingString.lastIndexOf("]") + 1, incomingString.length());

        	Set<String> clientsIps = new HashSet<String>();
        	clientsIps =  redis.smembers(appId);
        	
        	for(String tmpClientIp :  clientsIps){
        		//对该应用下的所有客户端机器，进行信息推送
        		Channel tmpClientChannel = this.mapChannelsSession.get(tmpClientIp);

        		//tmpClientChannel.writeAndFlush("["+ userId + "]" + message + "/n");
        		
        		logger.info("需要向应用：" + appId + "进行推送，于是发起了对客户主机" + tmpClientIp + "的消息推送，使用的channel是：" + tmpClientChannel.id());
        		
        		for (Channel channel : channels) {
                    if (channel == tmpClientChannel){
                    	channel.writeAndFlush("["+ userId + "]" + message +"\n");
                    }
                }
        	}
        }

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception { //覆盖了 channelActive() 事件处理方法。服务端监听到客户端活动
        Channel incomingChannel = ctx.channel();
        logger.info("SimpleChatClient:"+incomingChannel.remoteAddress()+"在线,建立了来自client的channel，其id为：" + incomingChannel.id());
        
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        logger.info("SimpleChatClient:"+incoming.remoteAddress()+"掉线");
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // exceptionCaught() 事件处理方法是当出现 Throwable 对象才会被调用，即当 Netty 由于 IO 错误或者处理器在处理事件时抛出的异常时。在大部分情况下，捕获的异常应该被记录下来并且把关联的 channel 给关闭掉。然而这个方法的处理方式会在遇到不同异常的情况下有不同的实现，比如你可能想在关闭连接之前发送一个错误码的响应消息。
        Channel incomingChannel = ctx.channel();
        logger.info("SimpleChatClient:"+incomingChannel.remoteAddress()+"异常, channel的id是：" + incomingChannel.id());
        // 当出现异常就关闭连接
        cause.printStackTrace();
        ctx.close();
    }
}
