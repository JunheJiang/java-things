package com.cmg.java.阿里技术公众号.fairnessqueue.version6;

import com.cmg.java.阿里技术公众号.fairnessqueue.version3.Queue;

/**
 * * 解决线程中断退出的问题，线程校验中断状态的场景
 * * <p>
 * * JVM 通常只会在有限的几个场景检测线程的中断状态， wait, Thread.join, Thread.sleep；
 * * <p>
 * * JVM 在检测到线程中断状态 Thread.interrupted() 后，会清除中断标志，抛出 InterruptedException；
 * * <p>
 * * 通常为了保证线程对中断及时响应， run 方法中需要自主检测中断标志，中断线程，特别是对中断比较敏感需要保持类的不变式的场景；
 * <p>
 * <p>
 * <p>
 * 当等待线程中断退出时，捕获中断异常，通过 pollLock.notify 和 offerLock.notify 转发消息；
 * <p>
 * 通过在 finally 中恢复状态追踪变量；
 * <p>
 * 通过状态变量追踪可以解决读与读之间和写与写之间的锁竞争问题。
 * <p>
 * 以下考虑如果解决读与读之间和写与写之间的公平性问题。
 * *
 * *
 * *
 */
class FairnessBoundedBlockingQueue implements Queue {
    // 容量
    protected final int capacity;

    // 头指针，empty: head.next == tail == null
    protected Node head;

    // 尾指针
    protected Node tail;

    // guard: canPollCount, head, waitPollCount
    protected final Object pollLock = new Object();
    protected int canPollCount;
    protected int waitPollCount;

    // guard: canOfferCount, tail, waitOfferCount
    protected final Object offerLock = new Object();
    protected int canOfferCount;
    protected int waitOfferCount;

    public FairnessBoundedBlockingQueue(int capacity) {
        this.capacity = capacity;
        this.canPollCount = 0;
        this.canOfferCount = capacity;
        this.waitPollCount = 0;
        this.waitOfferCount = 0;
        this.head = new Node(null);
        this.tail = head;
    }

    // 如果队列已满，通过返回值标识
    public boolean offer(Object obj) throws InterruptedException {
        if (Thread.interrupted()) {
            throw new InterruptedException(); // 线程已中断，直接退出即可，防止中断线程竞争锁
        }
        synchronized (offerLock) {
            while (canOfferCount <= 0) {
                waitOfferCount++;
                try {
                    offerLock.wait();
                } catch (InterruptedException e) {
                    // 触发其他线程
                    offerLock.notify();
                    throw e;

                } finally {
                    waitOfferCount--;
                }
            }
            Node node = new Node(obj);
            tail.next = node;
            tail = node;
            canOfferCount--;
        }
        synchronized (pollLock) {
            ++canPollCount;
            if (waitPollCount > 0) {
                pollLock.notify();
            }
        }
        return true;
    }

    // 如果队列为空，阻塞等待
    public Object poll() throws InterruptedException {
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        Object result = null;
        synchronized (pollLock) {
            while (canPollCount <= 0) {
                waitPollCount++;
                try {
                    pollLock.wait();
                } catch (InterruptedException e) {
                    pollLock.notify();
                    throw e;
                } finally {
                    waitPollCount--;
                }
            }

            result = head.next.value;
            head.next.value = 0;
            // ignore head;
            head = head.next;
            canPollCount--;
        }
        synchronized (offerLock) {
            canOfferCount++;
            if (waitOfferCount > 0) {
                offerLock.notify();
            }
        }
        return result;
    }

    // 省略 Node 的定义
    class Node {
        Object value;
        com.cmg.java.阿里技术公众号.fairnessqueue.version6.FairnessBoundedBlockingQueue.Node next;

        Node(Object obj) {
            this.value = obj;
            next = null;
        }
    }
}
