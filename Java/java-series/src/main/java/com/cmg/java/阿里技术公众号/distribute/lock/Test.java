package com.cmg.java.阿里技术公众号.distribute.lock;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;

/**
 * 积分只扣一次
 */
@Slf4j
public class Test {
    @Data
    class ConcurrencyRequest {
     Integer concurrency;
    }

    public void invokeAllTask(ConcurrencyRequest request, Runnable task) {
        final CountDownLatch startCountDownLatch = new CountDownLatch(1);
        final CountDownLatch endCountDownLatch = new CountDownLatch(request.getConcurrency());
        for (int i = 0; i < request.getConcurrency(); i++) {
            Thread t = new Thread(() -> {
                try {
                    startCountDownLatch.await();
                    try {
                        task.run();
                    } finally {
                        endCountDownLatch.countDown();
                    }
                } catch (Exception ex) {
                    log.error("异常", ex);
                }
            });
            t.start();
        }
        startCountDownLatch.countDown();
        try {
            endCountDownLatch.await();
        } catch (InterruptedException ex) {
            log.error("线程异常中断", ex);
        }
    }
}
