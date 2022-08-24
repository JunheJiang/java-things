package com.ljp.server;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.concurrent.GlobalEventExecutor;

public class SimpleChatServerHandler extends SimpleChannelInboundHandler<String> {

    public static ChannelGroup channels =
            new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public static Map<String, Channel> mapChannels = new HashMap<String, Channel>();
    
    private String deviceId = "";
    private String requestClientChannelKey = "";

    // 每当从服务端收到新的客户端连接时，
    // 新客户端的 Channel 存入到ChannelGroup列表中，并通知列表中的其他客户端 Channel
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        for (Channel channel : channels) {
            channel.writeAndFlush("[SERVER] - "
                    + incoming.remoteAddress() + " 加入\n");
        }
        channels.add(ctx.channel());
        
    }

    // 每当从服务端收到客户端断开时，
    // 该客户端的 Channel 在 ChannelGroup 列表中移除，并通知列表中的其他客户端 Channel
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        for (Channel channel : channels) {
            channel.writeAndFlush("[SERVER] - "
                    + incoming.remoteAddress() + " 离开\n");
        }
        channels.remove(ctx.channel());
    }

    // 每当从服务端读到客户端写入信息时，
    // 将信息转发给其他客户端的 Channel。
    @Override
    protected void channelRead0(ChannelHandlerContext ctx
            , String incomingString) throws Exception {
        Channel incomingChannel = ctx.channel();
        deviceId = "";
        
        // 1,获取客户端的channel
        // 2,分别以key，value的形式，保存/更新deviceId以及channel实例到一个叫
        // sessionMap的map变量当中。
        // 3,到zookeeper集群中注册一个znode（或者到redis中记录也行），
        // 路径是(deviceId),值是(该Netty服务器ip:port [+ channelId] )。
        // 4,在redis缓存中，应该有一条记录，是<userId:deviceId>的key-value对。
        // 这样就可以通过 :
        // userId ---找到--->  deviceId ---找到--->
        // Netty服务器ip:port ---找到---> Netty服务器以及该服务器下的 sessionMap。
        // 通过deviceId，重新由sessionMap中取回Channel，使用该Channel进行消息推送。
        
        if(incomingString.length() > 4 &&
                (incomingString.substring(0, 4).equals("push") != true) &&
                    incomingString.indexOf("[") == 0 ){
        	System.out.println("------channel:" + incomingChannel.id()
                    + "，并且监听到其传来了设备号：" + incomingString
                    + "  -fid:" + incomingChannel.parent().id());
        	deviceId = incomingString.substring(incomingString.indexOf("[") + 1,
                    incomingString.lastIndexOf("]"));
            System.out.println("------deviceId:" + deviceId);
        	mapChannels.put(deviceId, incomingChannel);
        }
        
        if(incomingString.length() > 4 && incomingString.substring(0, 4)
                .equals("push")){
        	requestClientChannelKey =
                    incomingString.substring(incomingString.indexOf("[") + 1,
                            incomingString.lastIndexOf("]"));
            Channel clientChannel =  mapChannels.get(requestClientChannelKey);
            clientChannel.writeAndFlush("Success to push this information"
                    + " to you!");
        }
        
        
        for (Channel channel : channels) {
            if (channel != incomingChannel){
                channel.writeAndFlush("[" + incomingChannel.remoteAddress()
                        + "]" + incomingString + "\n");
            } else {
                channel.writeAndFlush("[you]" + incomingString + "\n");
            }
        }
        
        
        

        
        
    }

    // 服务端监听到客户端活动
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        System.out.println("SimpleChatClient:"+incoming.remoteAddress()
                +"在线,建立了来自client的channel，其id为：" + incoming.id());
    }

    // 服务端监听到客户端不活动
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        System.out.println("SimpleChatClient:"+incoming.remoteAddress()+"掉线");
    }


    // 异常捕获处理
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        Channel incoming = ctx.channel();
        System.out.println("SimpleChatClient:"+incoming.remoteAddress()
                +"异常, channel的id是：" + incoming.id());
        // 当出现异常就关闭连接
        cause.printStackTrace();
        ctx.close();
    }
}

class GatewayService {
    
    private static Map<String, SocketChannel> map = new ConcurrentHashMap<>();
    
    public static void addGatewayChannel(String id, SocketChannel gateway_channel){
        map.put(id, gateway_channel);
    }
    
    public static Map<String, SocketChannel> getChannels(){
        return map;
    }

    public static SocketChannel getGatewayChannel(String id){
        return map.get(id);
    }
    
    public static void removeGatewayChannel(String id){
        map.remove(id);
    }
}



