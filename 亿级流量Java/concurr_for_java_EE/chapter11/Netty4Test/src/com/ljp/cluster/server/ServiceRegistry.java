package com.ljp.cluster.server;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import com.ljp.cluster.constance.Constant;


public class ServiceRegistry {
	private CountDownLatch latch = new CountDownLatch(0);


    public ServiceRegistry(String zkIP, String data) {
    	if (data != null) {
            ZooKeeper zk = connectServer(zkIP);
            if (zk != null) {
                createNode(zk, data);
            }
        }
    }
    
    //注册到zk中，其中data为服务端的 ip:port
//    public void register(String data) {
//        if (data != null) {
//            ZooKeeper zk = connectServer();
//            if (zk != null) {
//                createNode(zk, data);
//            }
//        }
//    }
    
    
    private ZooKeeper connectServer(String zkIP) {
        ZooKeeper zk = null;
        try {
           zk = new ZooKeeper(zkIP, Constant.ZK_SESSION_TIMEOUT, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    if (event.getState() == Event.KeeperState.SyncConnected) {
                        latch.countDown();
                    }
                }
            });
            latch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return zk;
    }
    
    private void createNode(ZooKeeper zk, String data) {
        try {
            byte[] dataBytes = data.getBytes();
            
			//zookeeper可以创建四种类型的znode： 
			//1、PERSISTENT-持久化目录节点 ，客户端与zookeeper断开连接后，该节点依旧存在 
			//2、PERSISTENT_SEQUENTIAL-持久化顺序编号目录节点 ，客户端与zookeeper断开连接后，该节点依旧存在，只是Zookeeper给该节点名称进行顺序编号 
			//3、EPHEMERAL-临时目录节点 ，客户端与zookeeper断开连接后，该节点被删除 
			//4、EPHEMERAL_SEQUENTIAL-临时顺序编号目录节点 
            String path = zk.create(Constant.NETTY_SERVER_ENTITYS_PATH + "/" + data.replace(":", "-").replace(".", "-"), dataBytes, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            System.out.println("create zookeeper node path:"+path+" data:"+data);
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }
}
