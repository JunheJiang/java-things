package com.cmg.java.亿级流量架构书.high_concurrency.queue.order;

import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

public class OrderBufferPublishThread extends Thread {
    boolean running;
    RoundRobinTable roundRobinTable;
    Map<RoundRobinTable.Table, Long> lastIdMap = Maps.newHashMap();
    Map<RoundRobinTable.Table, Object> lastOrderMap = Maps.newHashMap();

    @Override
    public void run() {
        while (running) {
            RoundRobinTable.Table table = roundRobinTable.nextTable();
            //批量查询缓冲表 处理中 worker ip jvm ip
            List<Map<String, Object>> list =
                    listOrderBuffers(table, lastIdMap.get(table));
            //发布缓冲订单
            list.forEach((map) -> {
                Long id = (Long) map.get("id");
                Long orderId = (Long) map.get("orderId");
                String orderJson = (String) map.get("orderJson");
                publishEvent(table, id, orderId, orderJson);
                lastIdMap.put(table,id);
                lastOrderMap.put(table,orderId);
                //限流？
                tryRateLimit();
            });

        }
    }

    private void tryRateLimit() {
    }

    private void publishEvent(RoundRobinTable.Table table, Long id, Long orderId, String orderJson) {
    }

    private List<Map<String, Object>> listOrderBuffers(RoundRobinTable.Table table, Long aLong) {
        return null;
    }
}
