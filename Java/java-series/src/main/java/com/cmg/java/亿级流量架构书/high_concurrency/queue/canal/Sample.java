//package com.cmg.java.architecture.high_concurrency.queue.canal;
//
//import com.alibaba.otter.canal.client.CanalConnector;
//import com.alibaba.otter.canal.client.CanalConnectors;
//
//public class Sample {
//    public static void main(String[] args) {
//        String zkServers = "";
//        String destination = "";
//        CanalConnector connector =
//                CanalConnectors.newClusterConnector(zkServers,
//                        destination, "", "");
//        connector.connect();
//        //写模式
//        connector.subscribe("product_.*\\.product_*.*");
//        while (true) {
////            Message message = connector.getWithoutAck(1000);
////            for(Entry
////            }
//        }
//    }
//}
