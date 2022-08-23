package com.cmg.java.亿级流量架构书.high_concurrency.async.controller.component;

import com.cmg.java.亿级流量架构书.high_concurrency.async.service.PriceService;
import com.netflix.hystrix.HystrixCollapser;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class BatchPriceCommand extends HystrixCommand<List<Double>> {

    private PriceService priceService;
    private Collection<HystrixCollapser.CollapsedRequest<Double, Long>> collection;

    public BatchPriceCommand(PriceService priceService, Collection<HystrixCollapser.CollapsedRequest<Double, Long>> collection) {
        super(setter());
        this.priceService = priceService;
        this.collection = collection;
    }

    public static Setter setter() {
        return Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("price"));
    }

    @Override
    protected List<Double> run() throws Exception {
        List<Long> ids = collection.stream()
                .map(req -> {
                    Long id = req.getArgument();
                    log.info("" + id);
                    return id;
                })
                .collect(Collectors.toList());
        return priceService.getPrices(ids);
    }
}
