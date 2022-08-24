package com.ljp.client;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.ljp.netty.common.SocketModel;
import com.ljp.server.PusherServer02;
import com.ljp.util.ReadPropertiesUtil;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import redis.clients.jedis.Jedis;

public class PusherClientHandler extends  SimpleChannelInboundHandler<SocketModel> {
	
	// 定义客户端没有收到服务端的pong消息的最大次数  
    private static final int MAX_UN_REC_PONG_TIMES = 5;  
      
    // 隔N秒后重连  
    private static final int RE_CONN_WAIT_SECONDS = 15;  
      
    // 客户端连续N次没有收到服务端的pong消息  计数器  
    private int unRecPongTimes = 0 ;  
    
    // 是否停止  
    //private boolean isStop = false ;  
	
    private ScheduledExecutorService executorService ;  
    
	private static Logger logger = Logger.getLogger(PusherClientHandler.class);
	
	
	private void connServer(){  
        
        //isStop = false;  
        
        
        if(executorService!=null){  
            executorService.shutdown();  
        }  
        executorService = Executors.newScheduledThreadPool(1);  
        executorService.scheduleWithFixedDelay(new Runnable() {  
        	Channel tmpChannel = null; 
            boolean isConnSucc = true;  
              
            @Override  
            public void run() {  
                try {  
                    // 重置计数器  
                    unRecPongTimes = 0;  
                    // 连接服务端  
                    //实际的案例中，可能是通过F5分配Netty服务端
                	String nettyServerIp = ReadPropertiesUtil.getPropertiesValue("systemConfig.properties", "nettyServerIp");
                	String nettyServerPort = ReadPropertiesUtil.getPropertiesValue("systemConfig.properties", "nettyServerPort");
                    new PusherClient(nettyServerIp, Integer.parseInt(nettyServerPort)).run();

                    System.out.println("connect server finish");  
                } catch (Exception e) {  
                    e.printStackTrace();  
                    isConnSucc = false ;  
                } finally{  
                    if(isConnSucc){  
                        if(executorService!=null){  
                            executorService.shutdown();  
                        }  
                    }  
                }  
            }  
        }, RE_CONN_WAIT_SECONDS, RE_CONN_WAIT_SECONDS, TimeUnit.SECONDS);  
    } 
	
	
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, SocketModel socketModel) throws Exception {
    	
    	// 凡是有信息接收到（包括Pong信息），则读计数器清零  
        unRecPongTimes = 0; 

        if (socketModel.getType().equals("pong")) {  //心跳处理
            // 计数器清零  
        	logger.info("收到pong的信息");
            //unRecPongTimes = 0;  
        }  
    	
    	if( socketModel.getType().equals("websocketPush") ) {
    		String appId = socketModel.getMessage().get(0);
        	String userId = socketModel.getMessage().get(1);
        	String message = socketModel.getMessage().get(2);
            System.out.println("收到了服务器发起的消息推送：该信息需推送给应用【"+ appId +"】下的用户：" + userId + "，相关的信息内容是：" + message);
    	}
    	
    	
    }
    
    
    @Override  
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {  
        System.out.println("Client close ");  
        super.channelInactive(ctx);  
        //重连
        connServer(); 
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws IOException { // (7)
        Channel channel = ctx.channel();
        System.out.println("PusherServer:"+channel.remoteAddress() +"异常, channel的id是：" + channel.id());
        // 当出现异常就关闭连接
        //cause.printStackTrace();
        
        //（1）删除redis的信息记录；（2）关闭/删除该channel的上下文信息。
        String[] redisIpAndPort = ReadPropertiesUtil.getPropertiesValue("systemConfig.properties", "redis").split(":");
    	Jedis redis = new Jedis(redisIpAndPort[0], Integer.parseInt(redisIpAndPort[1]));
        
    	//记录更新应用与客户端机器信息列表对应关系到redis
        String clientIpInfo = ctx.channel().localAddress().toString();
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
            	logger.info("===客户端===(READER_IDLE 读超时)");
            } 
            else if (event.state() == IdleState.WRITER_IDLE)  {
            	//写超时   
            	logger.info("===客户端===(WRITER_IDLE 写超时)");  
                if(unRecPongTimes < MAX_UN_REC_PONG_TIMES){  
                	SocketModel pingModel = new SocketModel();
                	pingModel.setType("ping");
                    ctx.channel().writeAndFlush(pingModel) ;  
                    unRecPongTimes++;  
                }else{  
                    ctx.channel().close();  
                }  
            }
                
            
            else if (event.state() == IdleState.ALL_IDLE)  
                logger.info("all idle");  
        }  
    }
}