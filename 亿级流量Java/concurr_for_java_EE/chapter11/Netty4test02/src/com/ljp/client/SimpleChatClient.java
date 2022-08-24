package com.ljp.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

import com.ljp.util.ReadPropertiesUtil;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class SimpleChatClient {
    private final String host;
    private final int port;
    
    Logger logger = Logger.getLogger(SimpleChatClient.class);

    public SimpleChatClient(String host, int port){
        this.host = host;
        this.port = port;
    }

    public void run() throws Exception{
    	
    	String appId = ReadPropertiesUtil.getPropertiesValue("systemConfig.properties", "appId");

        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap  = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new SimpleChatClientInitializer());
            Channel channel = bootstrap.connect(host, port).sync().channel();
            logger.info("------获取到服务端的Channel，其id:"  + channel.id());
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            
            channel.writeAndFlush("[reg]" + appId + "\r\n");
            
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
    	// 指定Netty Server的IP、端口
        String nettyServerIp = "172.20.46.20";
    	int nettyServerPort = 8081;
    	
    	if(args.length > 0){
    		nettyServerIp = args[0];
    		nettyServerPort = Integer.parseInt(args[1]);
    	}
    	
        new SimpleChatClient(nettyServerIp, nettyServerPort).run();
    }
}
