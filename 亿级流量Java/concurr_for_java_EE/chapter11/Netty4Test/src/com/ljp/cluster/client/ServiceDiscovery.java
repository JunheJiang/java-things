package com.ljp.cluster.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event;
import org.apache.zookeeper.ZooKeeper;

import com.ljp.cluster.constance.Constant;

import io.netty.util.internal.ThreadLocalRandom;

public class ServiceDiscovery {
	private CountDownLatch latch = new CountDownLatch(1);
    private volatile List<String> dataList = new ArrayList<String>();
    
    public ServiceDiscovery(String zkIP) {
        ZooKeeper zk = connectServer(zkIP);
        if (zk != null) {
            watchNode(zk);
        }
    }
    
    //服务发现接口
    public String discover() {
        String data = null;
        int size = dataList.size();
        if (size > 0) {
            if (size == 1) {
                data = dataList.get(0);
            } else {
             //随机获取其中的一个
                data = dataList.get(ThreadLocalRandom.current().nextInt(size));
            }
        }
        return data;
    }
    
    
    private ZooKeeper connectServer(String zkIP) {
        ZooKeeper zk = null;
        
        try {
         //format host1:port1,host2:port2,host3:port3
            zk = new ZooKeeper(zkIP, Constant.ZK_SESSION_TIMEOUT, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                 //zookeeper处于同步连通的状态时
                    if (event.getState() == Event.KeeperState.SyncConnected) {
                        latch.countDown();
                    }
                }
            });
            latch.await();
        } catch (IOException e) {
        	e.printStackTrace();
        } catch (InterruptedException e) {
        	e.printStackTrace();
        }
        return zk;
    }
    
    
    private void watchNode(final ZooKeeper zk) {
        try {
            List<String> nodeList = zk.getChildren(Constant.NETTY_SERVER_ENTITYS_PATH, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    if (event.getType() == Event.EventType.NodeChildrenChanged) {
                        watchNode(zk);
                    }
                }
            });
            List<String> dataList = new ArrayList<String>();
            for (String node : nodeList) {
                byte[] bytes = zk.getData(Constant.NETTY_SERVER_ENTITYS_PATH + "/" + node, false, null);
                dataList.add(new String(bytes));
            }
            
            System.out.println("------ netty server entitys count:" + dataList.size());
            this.dataList = dataList;
        } catch (Exception e) {
         e.printStackTrace();
        }
    }
}
