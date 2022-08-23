package com.cmg.java.亿级流量架构书.high_concurrency.async.controller.component;

import com.cmg.java.亿级流量架构书.high_concurrency.async.service.ProductService;
import com.netflix.hystrix.*;

//jvm缓存
//请求级别的缓存实现
public class GetProductServiceCommand extends HystrixCommand<String> {
    private ProductService productService;
    private Long id;

    public GetProductServiceCommand(ProductService productService, Long id) {
        super(setter());
        this.id = id;
        this.productService = productService;
    }

    private static Setter setter() {
        //服务分组
        HystrixCommandGroupKey groupKey = HystrixCommandGroupKey.Factory.asKey("stock");
        //服务标识
        HystrixCommandKey commandKey = HystrixCommandKey.Factory.asKey("getStock");
        //线程池名称
        HystrixThreadPoolKey poolKey = HystrixThreadPoolKey.Factory.asKey("stock-pool");
        //线程池配置
        HystrixThreadPoolProperties.Setter poolProperties = HystrixThreadPoolProperties.Setter().
                withCoreSize(10).withKeepAliveTimeMinutes(5).withMaxQueueSize(Integer.MAX_VALUE)
                .withQueueSizeRejectionThreshold(10000);
        //命令属性配置
        HystrixCommandProperties.Setter commandProperties =
                HystrixCommandProperties.Setter()
                        //启用降级处理
                        .withFallbackEnabled(true)
                        .withExecutionIsolationThreadInterruptOnFutureCancel(true)
                        .withExecutionIsolationThreadInterruptOnTimeout(true)
                        .withExecutionTimeoutEnabled(true)
                        .withExecutionTimeoutInMilliseconds(1000)
                        .withExecutionIsolationStrategy(
                                HystrixCommandProperties.ExecutionIsolationStrategy.THREAD)
                        .withRequestCacheEnabled(true);
        return HystrixCommand.Setter.withGroupKey(groupKey)
                .andCommandKey(commandKey)
                .andThreadPoolKey(poolKey)
                .andThreadPoolPropertiesDefaults(poolProperties)
                .andCommandPropertiesDefaults(commandProperties);
    }

    @Override
    protected String run() throws Exception {
        return productService.getProduct(this.id);
    }

    @Override
    protected String getCacheKey() {
        return "product:" + id;
    }
}
