package com.cmg.java.阿里技术公众号.fairnessqueue.version3;

public interface Queue {

    boolean offer(Object obj) throws InterruptedException;

    Object poll() throws InterruptedException;
}
