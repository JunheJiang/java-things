package com.ljp.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.ljp.netty.common.SocketModel;
import com.ljp.util.ReadPropertiesUtil;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class PusherClient {
    private final String host;
    private final int port;
    
    public static Logger  logger = Logger.getLogger(PusherClient.class);

    public PusherClient(String host, int port){
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
                    .handler(new PusherClientInitializer());
            Channel channel = bootstrap.connect(host, port).sync().channel();
            logger.info("------成功连接上Netty服务端，并新建立了一个由Netty客户端发起的，与Netty服务端绑定的Channel，其id为:"  + channel.id());
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            
            SocketModel socketModel = new SocketModel();
            socketModel.setType("reg");
            List<String> listMessage = new ArrayList<String>();	
            listMessage.add(appId);
            
            socketModel.setMessage(listMessage);     
            
            channel.writeAndFlush(socketModel);
            
            while(true){
                //保持长连接
            	Thread.sleep(1000L);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }

    }
    
    public static void main(String[] args) throws Exception{
    	//实际的案例中，可能是通过F5分配Netty服务端
    	String nettyServerIp = ReadPropertiesUtil.getPropertiesValue("systemConfig.properties", "nettyServerIp");
    	String nettyServerPort = ReadPropertiesUtil.getPropertiesValue("systemConfig.properties", "nettyServerPort");
    	
        new PusherClient(nettyServerIp, Integer.parseInt(nettyServerPort)).run();
    }
}
