package com.cmg.java.亿级流量架构书.high_concurrency.queue.order;

import com.lmax.disruptor.EventHandler;

public class OrderBufferHandler implements EventHandler<OrderBufferEvent> {
    @Override
    public void onEvent(OrderBufferEvent orderBufferEvent, long l, boolean b) throws Exception {
        Long orderId = orderBufferEvent.getOrderId();
        String orderJson = orderBufferEvent.getOrderJson();
        RoundRobinTable.Table table = orderBufferEvent.getTable();
        try {
            //同步缓冲订单到订单中心
            //操作成功删除缓冲订单

        } catch (Exception e) {
            //是否已经插入订单中心数据库、是则直接删除缓冲订单
        }
    }
}
