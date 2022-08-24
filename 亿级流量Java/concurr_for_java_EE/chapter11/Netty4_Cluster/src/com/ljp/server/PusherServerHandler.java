package com.ljp.server;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import com.ljp.netty.common.SocketModel;
import com.ljp.util.ReadPropertiesUtil;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.concurrent.GlobalEventExecutor;
import redis.clients.jedis.Jedis;

public class PusherServerHandler  extends SimpleChannelInboundHandler<SocketModel> { // (1)
	
	// 定义没有收到服务端的ping消息的最大次数  
    private static final int MAX_UN_REC_PING_TIMES = 5;  
    
    // 失败计数器：未收到client端发送的ping请求  
    private int unRecPingTimes = 0 ; 

    public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    
    static Logger logger = Logger.getLogger(PusherServerHandler.class);
    
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {  //覆盖了 handlerAdded() 事件处理方法。每当从服务端收到新的客户端连接时， 与客户端机器信息与channel一起保存到服务器的sessionMap中
        Channel incomingChannel = ctx.channel();
        String clientIpInfo = incomingChannel.remoteAddress().toString();
        
        channels.add(ctx.channel());
        logger.info("channel:" + incomingChannel.id() + "已加入");

    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {  // (3)
        Channel incomingChannel = ctx.channel();
        channels.remove(ctx.channel());
        logger.info("channel:" + incomingChannel.id() + "已移除");
        
    }
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, SocketModel socketModel) throws Exception { // 覆盖了 channelRead0() 事件处理方法。每当从服务端读到客户端写入信息时，将信息转发给其他客户端的 channel
        Channel incomingChannel = ctx.channel();
        
        // 凡是有信息接收到（包括ping信息），失败计数器清零  
        unRecPingTimes = 0;  
        
        // 心跳处理，收到ping消息后，回复  
        if(socketModel.getType().equals("ping")){  
        	logger.info("收到了ping信息");
        	socketModel.setType("pong");
        	socketModel.setCode("OK!");  
            ctx.channel().writeAndFlush(socketModel);  
        }
        
        
 
        //记录或者更新：应用与客户端机器信息列表对应关系到redis
        String clientIpInfo = incomingChannel.remoteAddress().toString();
        
        String[] redisIpAndPort = ReadPropertiesUtil.getPropertiesValue("systemConfig.properties", "redis").split(":");
    	Jedis redis = new Jedis(redisIpAndPort[0], Integer.parseInt(redisIpAndPort[1]));
    	
    	

        if(socketModel.getType().equals("reg")){//如果是注册，则执行
        	String appId = socketModel.getMessage().get(0);
        	redis.sadd(appId, clientIpInfo);
        	logger.info("有应用进行了注册操作：" + appId);
        	//辅助用途的redis记录
        	redis.set(clientIpInfo, appId);
        }
        
 
        redis.close();
    }

    
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception { //覆盖了 channelActive() 事件处理方法。服务端监听到客户端活动
        Channel incomingChannel = ctx.channel();
        logger.info("PusherClient:"+incomingChannel.remoteAddress()+"在线,建立了来自client的channel，其id为：" + incomingChannel.id());
        
    }

    
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        logger.info("PusherClient:"+incoming.remoteAddress()+"掉线");
    }
    
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws IOException { // exceptionCaught() 事件处理方法是当出现 Throwable 对象才会被调用，即当 Netty 由于 IO 错误或者处理器在处理事件时抛出的异常时。在大部分情况下，捕获的异常应该被记录下来并且把关联的 channel 给关闭掉。然而这个方法的处理方式会在遇到不同异常的情况下有不同的实现，比如你可能想在关闭连接之前发送一个错误码的响应消息。
        Channel incomingChannel = ctx.channel();
        logger.info("PusherClient:"+incomingChannel.remoteAddress()+"异常, channel的id是：" + incomingChannel.id());
        // 当出现异常就关闭连接
        cause.printStackTrace();
        
        //（1）删除redis的信息记录；（2）关闭/删除该channel的上下文信息。
        String[] redisIpAndPort = ReadPropertiesUtil.getPropertiesValue("systemConfig.properties", "redis").split(":");
    	Jedis redis = new Jedis(redisIpAndPort[0], Integer.parseInt(redisIpAndPort[1]));
        
    	//记录更新应用与客户端机器信息列表对应关系到redis
        String clientIpInfo = incomingChannel.remoteAddress().toString();
        String appId = redis.get(clientIpInfo);
        redis.srem(appId, clientIpInfo);
        redis.del(clientIpInfo);
    	redis.close();
        ctx.close();
    }
    
    
    
    @Override  
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {  
        if (IdleStateEvent.class.isAssignableFrom(evt.getClass())) {  
            IdleStateEvent event = (IdleStateEvent) evt;  
            if (event.state() == IdleState.READER_IDLE)  {
            	//读超时  
                logger.info("===服务端===(READER_IDLE 读超时)");  
                if(unRecPingTimes >= MAX_UN_REC_PING_TIMES){  
                    ctx.channel().close();  
                }else{  
                    // 失败计数器加1  
                    unRecPingTimes++;  
                }  
            }
            else if (event.state() == IdleState.WRITER_IDLE)  
                logger.info("write idle");  
            else if (event.state() == IdleState.ALL_IDLE)  
                logger.info("all idle");  
        }  
    }
    

}
