package com.cmg.java.亿级流量架构书.high_concurrency.async.service;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface AsyncService {
    List asyncArrange3ToFinal1() throws ExecutionException, InterruptedException;

    void asyncArrange2FistThen1() throws ExecutionException, InterruptedException;

    void asyncArrange1Then2Final1();
}
