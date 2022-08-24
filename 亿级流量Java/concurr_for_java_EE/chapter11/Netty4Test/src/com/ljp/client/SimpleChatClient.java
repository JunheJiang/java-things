package com.ljp.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class SimpleChatClient {
    private final String host;
    private final int port;

    public SimpleChatClient(String host, int port){
        this.host = host;
        this.port = port;
    }

    public void run() throws Exception{
    	//这里假设获取到了手机的deviceid
    	String deviceId = "IMEI009912563" + Math.round((Math.random() * 1000));
    	
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap  = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new SimpleChatClientInitializer());
            Channel channel = bootstrap.connect(host, port).sync().channel();
            System.out.println("------获取到服务端的Channel，其id:"
                    + channel.id());
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(System.in)
            );
            
            channel.writeAndFlush("[" + deviceId + "]\r\n");
            
            while(true){
                channel.writeAndFlush(in.readLine() + "\r\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }

    }
    
    public static void main(String[] args) throws Exception{
        new SimpleChatClient("172.20.46.20", 8081).run();
    }
}
