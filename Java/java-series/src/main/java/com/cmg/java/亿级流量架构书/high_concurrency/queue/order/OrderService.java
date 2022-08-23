package com.cmg.java.亿级流量架构书.high_concurrency.queue.order;

import com.alibaba.fastjson.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;

public class OrderService {
    RoundRobinTable roundRobinTable;
    OrderCache orderCache;

    public void submitOrder(OrderDto orderDto) {
        RoundRobinTable.Table table = roundRobinTable.nextTable();
        String sql = getSql(table);
        JdbcTemplate template = new JdbcTemplate(table.getDataSource());
        String oderJson = JSONObject.toJSONString(orderDto);
        Long orderId = orderDto.getOrderId();
        template.update(sql, orderId, oderJson);
        //放入缓存
        orderCache.put(orderDto);
    }

    private String getSql(RoundRobinTable.Table table) {
        return null;
    }

}
