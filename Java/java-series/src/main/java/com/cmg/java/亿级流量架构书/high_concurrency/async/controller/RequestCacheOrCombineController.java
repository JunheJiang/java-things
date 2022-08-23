package com.cmg.java.亿级流量架构书.high_concurrency.async.controller;

import com.cmg.java.亿级流量架构书.high_concurrency.async.controller.component.GetPriceServiceCommand;
import com.cmg.java.亿级流量架构书.high_concurrency.async.controller.component.GetProductServiceCommand;
import com.cmg.java.亿级流量架构书.high_concurrency.async.service.PriceService;
import com.cmg.java.亿级流量架构书.high_concurrency.async.service.ProductService;
import com.google.common.collect.Maps;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RestController
@Slf4j
public class RequestCacheOrCombineController {
    @Autowired
    ProductService productService;
    @Autowired
    PriceService priceService;

    @GetMapping("/product/info")
    public void requestCache() {
        HystrixRequestContext context = HystrixRequestContext.initializeContext();
        try {
            GetProductServiceCommand command1 = new GetProductServiceCommand(productService, 1l);
            GetProductServiceCommand command2 = new GetProductServiceCommand(productService, 1l);
            command1.execute();
            command2.execute();
            GetProductServiceCommand command3 = new GetProductServiceCommand(productService, 1l);
            command3.execute();
            if (!command1.isResponseFromCache()) {
                log.info("first request、not from cache");
            }
            if (command2.isResponseFromCache()) {
                log.info("second request、 yes from cache");
            }
            if (command3.isResponseFromCache()) {
                log.info("third request、yes from cache");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            context.shutdown();
        }
    }

    @PostMapping("/product/clientBatchQuery")
    public void clientBatchQuery(@RequestBody List<Long> ids) throws ExecutionException, InterruptedException {
        HystrixRequestContext context = HystrixRequestContext.initializeContext();
        List<CompletableFuture<String>> futures = Lists.newArrayList();
        Map map = Maps.newConcurrentMap();
        try {
            for (Long id : ids) {
                futures.add(CompletableFuture.supplyAsync(() -> {
                    GetProductServiceCommand command1 = new GetProductServiceCommand(productService, id);
                    String str = command1.execute();
                    log.info(str);
                    map.put(id, str);
                    return str;
                }));
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            context.shutdown();
        }
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    }

    @GetMapping("/product/combineRequest")
    public void combineRequest() {
        HystrixRequestContext context = HystrixRequestContext.initializeContext();
        try {
            GetPriceServiceCommand command1 = new GetPriceServiceCommand(priceService, 1l);
            GetPriceServiceCommand command2 = new GetPriceServiceCommand(priceService, 2l);
            GetPriceServiceCommand command3 = new GetPriceServiceCommand(priceService, 3l);
            Future<Double> f1 = command1.queue();
            Future<Double> f2 = command2.queue();
            Future<Double> f3 = command3.queue();
            f1.get();
            f2.get();
            f3.get();
            //将多个单个查询合并后批量查询
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            context.shutdown();
        }
    }
}
