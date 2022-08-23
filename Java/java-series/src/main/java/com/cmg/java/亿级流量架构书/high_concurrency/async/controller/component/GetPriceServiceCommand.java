package com.cmg.java.亿级流量架构书.high_concurrency.async.controller.component;

import com.cmg.java.亿级流量架构书.high_concurrency.async.service.PriceService;
import com.netflix.hystrix.HystrixCollapser;
import com.netflix.hystrix.HystrixCollapserKey;
import com.netflix.hystrix.HystrixCollapserProperties;
import com.netflix.hystrix.HystrixCommand;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class GetPriceServiceCommand extends HystrixCollapser<List<Double>, Double, Long> {
    private PriceService priceService;
    private Long id;

    public GetPriceServiceCommand(PriceService priceService, Long id) {
        super(setter());
        this.priceService = priceService;
        this.id = id;
    }

    private static Setter setter() {
        return HystrixCollapser.Setter
                .withCollapserKey(HystrixCollapserKey.Factory.asKey("price"))
                .andCollapserPropertiesDefaults(HystrixCollapserProperties
                        //两个合并为一批次
                        .Setter().withMaxRequestsInBatch(2)
                        //5毫秒正好两个请求立即执行
                        //5毫秒内小于2个 等到2个才执行、过了5毫秒还不来 先行告辞
                        .withTimerDelayInMilliseconds(5)
                        .withRequestCacheEnabled(true))
                .andScope(Scope.REQUEST);
    }


    @Override
    public Long getRequestArgument() {
        return id;
    }

    @Override
    protected HystrixCommand<List<Double>> createCommand(Collection<CollapsedRequest<Double, Long>> collection) {
        return new BatchPriceCommand(priceService, collection);
    }

    @Override
    protected void mapResponseToRequests(List<Double> doubles, Collection<CollapsedRequest<Double, Long>> collection) {
        final AtomicInteger count = new AtomicInteger(0);
        collection.forEach((request) -> {
            log.info("1234");
            request.setResponse(doubles.get(count.getAndIncrement()));
        });

    }
}
