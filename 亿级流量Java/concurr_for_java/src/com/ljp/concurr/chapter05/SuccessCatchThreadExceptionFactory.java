package com.ljp.concurr.chapter05;

import java.util.concurrent.ThreadFactory;

public class SuccessCatchThreadExceptionFactory
        implements ThreadFactory {
    @Override
    public Thread newThread(Runnable runnable) {
        Thread tmpThread = new Thread(runnable);
        tmpThread.setUncaughtExceptionHandler(
            new SuccessCatchThreadExceptionHandler()
        );
        return tmpThread;
    }
}
