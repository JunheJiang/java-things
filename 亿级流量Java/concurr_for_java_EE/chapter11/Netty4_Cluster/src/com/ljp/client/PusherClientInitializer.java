package com.ljp.client;

import com.ljp.netty.common.LengthDecoder;
import com.ljp.netty.common.MessageDecoder;
import com.ljp.netty.common.MessageEncoder;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

public class PusherClientInitializer extends ChannelInitializer<SocketChannel> {
	
	private final static int READER_IDEL_SECONDS = 10;//读操作空闲30秒 ,快速测试的时候可以改为10秒 
    private final static int WRITER_IDEL_SECONDS = 20;//写操作空闲30秒 ,快速测试的时候可以改为20秒 
    private final static int ALL_IDEL_SECONDS = 30;//读写全部空闲100秒 ,快速测试的时候可以改为30秒 

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        
        pipeline.addLast(new LengthDecoder(1024,0,4,0,4));
        pipeline.addLast("decoder", new MessageDecoder());
        pipeline.addLast("encoder", new MessageEncoder());
        pipeline.addLast("ping", new IdleStateHandler(READER_IDEL_SECONDS, WRITER_IDEL_SECONDS,ALL_IDEL_SECONDS));  
        pipeline.addLast("handler", new PusherClientHandler());
        
        
    }
}
