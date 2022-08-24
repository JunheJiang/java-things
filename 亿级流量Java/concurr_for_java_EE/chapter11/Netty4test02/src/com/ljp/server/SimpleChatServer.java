package com.ljp.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class SimpleChatServer {

    private int port;

    public SimpleChatServer(int port) {
        this.port = port;
    }

    public void run() throws Exception {
    	/*
    	  NioEventLoopGroup是用来处理I/O操作的多线程事件循环器，Netty 提供了许多不同的EventLoopGroup的实现用来处理不同的传输。
    	   在这个例子中我们实现了一个服务端的应用，因此会需要2个 NioEventLoopGroup。第一个经常被叫做‘boss’，
    	   用来接收进来的连接。第二个经常被叫做‘worker’，用来处理已经被接收的连接，一旦‘boss’接收到连接，
    	   就会把连接信息注册到‘worker’上。如何知道多少个线程已经被使用，如何映射到已经创建的 Channel上都需要
    	   依赖于 EventLoopGroup 的实现，并且可以通过构造函数来配置他们的关系。
    	*/
        EventLoopGroup bossGroup = new NioEventLoopGroup();    
        EventLoopGroup workerGroup = new NioEventLoopGroup();  
        
        try {
        	/*
        	 ServerBootstrap是一个启动 NIO 服务的辅助启动类。
        	*/
            ServerBootstrap serverBootstrap = new ServerBootstrap(); 
            serverBootstrap.group(bossGroup, workerGroup)
             .channel(NioServerSocketChannel.class) //这里我们指定使用NioServerSocketChannel类来举例说明一个新的 Channel 如何接收进来的连接。
             .childHandler(new SimpleChatServerInitializer()) //这里的事件处理类经常会被用来处理一个最近的已经接收的 Channel。
             												  //SimpleChatServerInitializer 继承自ChannelInitializer是一个特殊的处理类，
             												  //他的目的是帮助使用者配置一个新的 Channel。
             												  //也许你想通过增加一些处理类比如 SimpleChatServerHandler 来配置一个新的 Channel 
             												  //或者其对应的ChannelPipeline来实现你的网络程序。
             												  //当你的程序变的复杂时，可能你会增加更多的处理类到 pipline 上，
             												  //然后提取这些匿名类到最顶层的类上。
             
             .option(ChannelOption.SO_BACKLOG, 128)           // 你可以设置这里指定的 Channel 实现的配置参数。我们正在写一个TCP/IP 的服务端，
             												  //因此这里运行我们设置 socket 的参数选项，比如tcpNoDelay 和 keepAlive。
             												  //请参考ChannelOption和详细的ChannelConfig实现的接口文档
             												  //以此可以对ChannelOption 的有一个大概的认识。
             
             .childOption(ChannelOption.SO_KEEPALIVE, true);  //option() 是提供给NioServerSocketChannel接收到的连接使用。
            												  //childOption() 是提供给由父管道ServerChannel接收到的连接使用，
            												  //在这个例子中也是 NioServerSocketChannel。 

            System.out.println("SimpleChatServer 启动了");

            // 绑定端口，开始接收进来的连接
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync(); // 这里我们在机器上绑定了一个设置好的端口。当然现在你可以多次调用 bind() 方法(基于不同绑定地址)。
            // 等待服务器  socket 关闭 。
            // 在这个例子中，这不会发生，但你可以优雅地关闭你的服务器。
            channelFuture.channel().closeFuture().sync();

        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();

            System.out.println("SimpleChatServer 关闭了");
        }
    }

    public static void main(String[] args) throws Exception {
        int port = 8081;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }
        new SimpleChatServer(port).run();

    }
}
