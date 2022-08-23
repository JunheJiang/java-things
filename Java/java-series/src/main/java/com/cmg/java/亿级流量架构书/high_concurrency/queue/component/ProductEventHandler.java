package com.cmg.java.亿级流量架构书.high_concurrency.queue.component;

import com.lmax.disruptor.EventHandler;

public class ProductEventHandler implements EventHandler<String> {

    @Override
    public void onEvent(String s, long l, boolean b) throws Exception {

    }
}
