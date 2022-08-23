package com.cmg.java.亿级流量架构书.high_concurrency.queue.order;

import lombok.Data;

@Data
public class OrderBufferEvent {
    Long orderId;
    String orderJson;
    RoundRobinTable.Table table;
}
