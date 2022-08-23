package com.cmg.java.亿级流量架构书.high_concurrency.async.service.impl;

import com.cmg.java.亿级流量架构书.high_concurrency.async.service.AsyncService;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class AsyncServiceImpl implements AsyncService {
    //三个任务并发异步调用
    @Override
    public List asyncArrange3ToFinal1() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            try {
                return service1();
            } catch (Exception e) {
                log.error(e.getMessage());
            }
            return "service1";
        });
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            try {
                return service2();
            } catch (Exception e) {
                log.error(e.getMessage());
            }
            return "service2";
        });
        CompletableFuture<String> future3 = CompletableFuture.supplyAsync(() -> {
            try {
                return service3();
            } catch (Exception e) {
                log.error(e.getMessage());
            }
            return "service3";
        });
        //全部
        return CompletableFuture.allOf(future1, future2, future3)
                .thenApplyAsync((Void) -> {
                            try {
                                return Lists.newArrayList(future1.get(),
                                        future2.get(), future3.get());
                            } catch (InterruptedException e) {
                                log.error(e.getMessage());
                            } catch (ExecutionException e) {
                                log.error(e.getMessage());
                            }
                            return new ArrayList<>();
                        }
                ).exceptionally(e -> {
                    log.error(e.getMessage());
                    return new ArrayList<>();
                }).get();
    }

    //两个任务并发调用然后消费结果
    @Override
    public void asyncArrange2FistThen1() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            try {
                return service1();
            } catch (Exception e) {
                log.error(e.getMessage());
            }
            return "service1";
        });
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            try {
                return service2();
            } catch (Exception e) {
                log.error(e.getMessage());
            }
            return "service2";
        });
        //两者并发执行
        future1.thenAcceptBothAsync(future2, (future1Result, future2Result) -> {
            String result = future1Result + "、" +
                    future2Result;
            log.info(result);
        }).exceptionally(e -> {
            log.error(e.getMessage());
            return null;
        }).get();
    }

    //先执行第一个后与后两个到执行结果一并执行
    @Override
    public void asyncArrange1Then2Final1() {
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            try {
                String result = service1();
                log.info(result);
                return result;
            } catch (Exception e) {
                log.error(e.getMessage());
            }
            return "service1";
        });

        CompletableFuture<String> future2 = future1.thenApplyAsync((future1Result) -> {
            log.info(future1Result);
            String result = service2();
            return future1Result + "-" + result;
        });

        CompletableFuture<String> future3 = CompletableFuture.supplyAsync(() -> {
            try {
                String result = service3();
                log.info(result);
                return result;
            } catch (Exception e) {
                log.error(e.getMessage());
            }
            return "service3";
        });
        future2.thenCombineAsync(future3, (future2Result, future3Result) -> {
            String result = future2Result + "、" +
                    future3Result;
            log.info(result);
            return result;
        }).exceptionally(e -> {
            log.error(e.getMessage());
            return null;
        });

    }

    public String service1() {
        return "service1 done";
    }

    public String service2() {
        return "service2 done";
    }

    public String service3() {
        return "service3 done";
    }

}
