package com.ljp.cluster.server;

import com.ljp.cluster.constance.Constant;
import com.ljp.server.SimpleChatServer;

public class ServerStart {
	
	 public static void main(String[] args) throws Exception{
		 
		  
		  
		  String zkIP = "";
		  String nettyServerIP = "";
		  String nettyPort = "";
		  

		  if( args.length > 0 ){
			zkIP = args[0];
			nettyServerIP = args[1];
			nettyPort = args[2];
		  }else{
			zkIP = Constant.ZK_IP;
			nettyServerIP = "172.20.177.117";
			nettyPort = "9006";
		  }
		  
		  
		  
		  String serverIpAndPort = nettyServerIP + ":" + nettyPort;
		  
		  //register service in zookeeper 
		  new SimpleChatServer( Integer.parseInt(nettyPort) ).run();
		  ServiceRegistry zsr = new ServiceRegistry(Constant.ZK_IP, serverIpAndPort);
		  
		  
		 
		  
	 }
}
