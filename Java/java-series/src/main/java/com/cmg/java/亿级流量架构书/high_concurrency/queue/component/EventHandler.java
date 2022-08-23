package com.cmg.java.亿级流量架构书.high_concurrency.queue.component;


import lombok.Data;

/**
 *
 */
@Data
public class EventHandler {
    ProductEventHandler productEventHandler;

    public void onEvent(String key, String type, EventQueue queue) {
    }
}
