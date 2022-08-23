package com.cmg.java.亿级流量架构书.high_availlable.limiting.controlller;

import com.cmg.java.亿级流量架构书.high_availlable.limiting.component.LimitingComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
public class LimitingController {
    @Autowired
    LimitingComponent limitingComponent;

    @GetMapping("/limitInterfaceTotalReq")
    public void limitInterfaceTotalReq() {
        limitingComponent.limitInterfaceTotalReq();
    }

    @GetMapping("/limitTimeWindowReq")
    public void limitTimeWindowReq() throws ExecutionException {
        limitingComponent.limitTimeWindowReq();
    }

    @GetMapping("/smoothBursty")
    public void smoothBursty() {
        limitingComponent.smoothBursty();
    }

    @GetMapping("/smoothWarmingUp")
    public void smoothWarmingUp() {
        limitingComponent.smoothWarmingUp();
    }
}
