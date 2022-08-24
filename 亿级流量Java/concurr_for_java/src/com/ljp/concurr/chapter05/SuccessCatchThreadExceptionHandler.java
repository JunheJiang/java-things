package com.ljp.concurr.chapter05;

public class SuccessCatchThreadExceptionHandler
        implements Thread.UncaughtExceptionHandler {
    @Override
    public void uncaughtException(Thread thread, Throwable e) {
        System.out.println(thread.getName()
                + "抛出了异常信息: " + e.toString());
    }
}
