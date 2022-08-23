package com.cmg.java.亿级流量架构书.high_availlable.isolation.component;

import com.cmg.java.亿级流量架构书.high_availlable.isolation.service.StockService;
import com.netflix.config.ConfigurationManager;
import com.netflix.hystrix.*;
import org.apache.commons.configuration.Configuration;

import java.util.concurrent.Future;

/**
 * 隔离获取库存服务命令
 */
public class GetStockServiceCommand extends HystrixCommand<Integer> {
    private StockService stockService;
    private String id;

    protected GetStockServiceCommand() {
        super(setter());
    }

    public GetStockServiceCommand(StockService stockService, String id) {
        super(setter());
        this.stockService = stockService;
        this.id = id;
    }

    @Override
    protected Integer run() throws Exception {
        return stockService.getStock(this.id);
    }

    //超时降级处理
    @Override
    protected Integer getFallback() {
        // 降级方法
        return 0;
    }

    //熔断降级处理 如果熔断了直接调用getFallback方法降级处理

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
                                HystrixCommandProperties.ExecutionIsolationStrategy.THREAD);
        return HystrixCommand.Setter.withGroupKey(groupKey)
                .andCommandKey(commandKey)
                .andThreadPoolKey(poolKey)
                .andThreadPoolPropertiesDefaults(poolProperties)
                .andCommandPropertiesDefaults(commandProperties);
    }

    /**
     * 调整配置
     */
    public void adjust() {
        String adjustKey = "hystrix.threadpool.stock-pool.queueSizeRejectionThreshold";
        Configuration configuration = ConfigurationManager.getConfigInstance();
        configuration.setProperty(adjustKey, 100);
    }

    public void useCommand() {
        GetStockServiceCommand command = new GetStockServiceCommand();
        //同步调用
        command.execute();
        //异步调用
        Future<Integer> future = command.queue();
    }

}
