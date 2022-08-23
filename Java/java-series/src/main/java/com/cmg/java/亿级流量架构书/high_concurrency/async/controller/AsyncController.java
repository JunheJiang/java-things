package com.cmg.java.亿级流量架构书.high_concurrency.async.controller;

import com.cmg.java.亿级流量架构书.high_concurrency.async.service.AsyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
public class AsyncController {
    @Autowired
    AsyncService asyncService;

    @GetMapping("/asyncArrange3ToFinal1")
    public List asyncArrange3ToFinal1() throws ExecutionException, InterruptedException {
        return asyncService.asyncArrange3ToFinal1();
    }

    @GetMapping("/asyncArrange2FistThen1")
    public void asyncArrange2FistThen1() throws ExecutionException, InterruptedException {
        asyncService.asyncArrange2FistThen1();
    }

    @GetMapping("/asyncArrange1Then2Final1")
    public void asyncArrange1Then2Final1() {
        asyncService.asyncArrange1Then2Final1();
    }
}
