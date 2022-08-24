package com.ljp.cluster.client;

import com.ljp.client.SimpleChatClient;
import com.ljp.cluster.constance.Constant;

public class ClientStart {
	 /**
	  * @param args
	 * @throws Exception 
	 * @throws NumberFormatException 
	  */
	 public static void main(String[] args) throws NumberFormatException, Exception {
		 
		 String zkIP = "";
		 
		 if( args.length > 0 ){
			 zkIP = args[0];
		 }else{
			 zkIP = Constant.ZK_IP;
		 }
		 
		 //find service from zookeeper
		 ServiceDiscovery sd = new ServiceDiscovery(zkIP);
		 String serverIp = sd.discover();
		 String[] serverArr = serverIp.split(":");
		 System.out.println("ServerIP:"+serverArr[0]+"    ServerPort:"+serverArr[1]);
		  

		 new SimpleChatClient(serverArr[0], Integer.valueOf(serverArr[1])).run();
		  
		  
	 }
}
