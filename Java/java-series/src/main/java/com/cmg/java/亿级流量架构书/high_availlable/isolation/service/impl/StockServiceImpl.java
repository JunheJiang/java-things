package com.cmg.java.亿级流量架构书.high_availlable.isolation.service.impl;

import com.cmg.java.亿级流量架构书.high_availlable.isolation.service.StockService;
import org.springframework.stereotype.Service;

@Service
public class StockServiceImpl implements StockService {
    @Override
    public int getStock(String id) {
        return 13333;
    }
}
