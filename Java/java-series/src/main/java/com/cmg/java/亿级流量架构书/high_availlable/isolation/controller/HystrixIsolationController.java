package com.cmg.java.亿级流量架构书.high_availlable.isolation.controller;

import com.cmg.java.亿级流量架构书.high_availlable.isolation.component.GetStockServiceCommand;
import com.cmg.java.亿级流量架构书.high_availlable.isolation.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * 隔离路线示例
 */
@RestController
public class HystrixIsolationController {
    @Autowired
    StockService stockService;

    @GetMapping("/getStock/{id}")
    public int getStock(@PathVariable String id) {
        GetStockServiceCommand command = new GetStockServiceCommand(stockService, id);
        //同步调用
        return command.execute();
    }
}
