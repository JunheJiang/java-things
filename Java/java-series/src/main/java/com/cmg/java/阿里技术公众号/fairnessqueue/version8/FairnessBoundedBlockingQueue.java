package com.cmg.java.阿里技术公众号.fairnessqueue.version8;

import com.cmg.java.阿里技术公众号.fairnessqueue.version3.Queue;

/**
 * 等待时间的问题
 * <p>
 * 并发场景下，等待通常会设置为限时等待 TIMED_WAITING ，避免死锁或损失系统活性；
 * <p>
 * 实现同步队列的限时等待，并没想象的那么困难
 * <p>
 * 由于所有的等待都阻塞在 WaitNode 监视器，以上
 * <p>
 * 首先定义超时异常，此处只是为了方便异常处理，继承 InterruptedException；
 * <p>
 * 此处依赖于 wait(long timeout) 的超时等待实现，这通常不是问题；
 * <p>
 * 最后，将 WaitNode 超时等待的逻辑，带入到 FairnessBoundedBlockingQueue 实现中，即可
 */

class FairnessBoundedBlockingQueue implements Queue {
    // 容量
    protected final int capacity;

    // 头指针，empty: head.next == tail == null
    protected com.cmg.java.阿里技术公众号.fairnessqueue.version8.FairnessBoundedBlockingQueue.Node head;

    // 尾指针
    protected com.cmg.java.阿里技术公众号.fairnessqueue.version8.FairnessBoundedBlockingQueue.Node tail;

    // guard: canPollCount, head, pollQueue
    protected final Object pollLock = new Object();
    protected int canPollCount;

    // guard: canOfferCount, tail, offerQueue
    protected final Object offerLock = new Object();
    protected int canOfferCount;

    protected final com.cmg.java.阿里技术公众号.fairnessqueue.version8.FairnessBoundedBlockingQueue.WaitQueue pollQueue = new com.cmg.java.阿里技术公众号.fairnessqueue.version8.FairnessBoundedBlockingQueue.WaitQueue();
    protected final com.cmg.java.阿里技术公众号.fairnessqueue.version8.FairnessBoundedBlockingQueue.WaitQueue offerQueue = new com.cmg.java.阿里技术公众号.fairnessqueue.version8.FairnessBoundedBlockingQueue.WaitQueue();

    public FairnessBoundedBlockingQueue(int capacity) {
        this.capacity = capacity;
        this.canOfferCount = capacity;
        this.canPollCount = 0;
        this.head = new com.cmg.java.阿里技术公众号.fairnessqueue.version8.FairnessBoundedBlockingQueue.Node(null);
        this.tail = head;
    }

    // 如果队列已满，通过返回值标识
    public boolean offer(Object obj) throws InterruptedException {
        if (Thread.interrupted()) {
            throw new InterruptedException(); // 线程已中断，直接退出即可，防止中断线程竞争锁
        }
        com.cmg.java.阿里技术公众号.fairnessqueue.version8.FairnessBoundedBlockingQueue.WaitNode wait = null;
        synchronized (offerLock) {
            // 在有阻塞请求或者队列为空时，阻塞等待
            if (canOfferCount <= 0 || !offerQueue.isEmpty()) {
                wait = new com.cmg.java.阿里技术公众号.fairnessqueue.version8.FairnessBoundedBlockingQueue.WaitNode();
                offerQueue.enq(wait);
            } else {
                // continue.
            }
        }

        try {
            if (wait != null) {
                wait.doWait(123);
            }
            if (Thread.interrupted()) {
                throw new InterruptedException();
            }
        } catch (InterruptedException e) {
            offerQueue.doNotify();
            throw e;
        }

        // 确保此时线程状态正常，以下不会校验中断
        synchronized (offerLock) {
            com.cmg.java.阿里技术公众号.fairnessqueue.version8.FairnessBoundedBlockingQueue.Node node = new com.cmg.java.阿里技术公众号.fairnessqueue.version8.FairnessBoundedBlockingQueue.Node(obj);
            tail.next = node;
            tail = node;
            canOfferCount--;
        }
        synchronized (pollLock) {
            ++canPollCount;
            pollQueue.doNotify();
        }
        return true;
    }

    // 如果队列为空，阻塞等待
    public Object poll() throws InterruptedException {
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        Object result = null;
        com.cmg.java.阿里技术公众号.fairnessqueue.version8.FairnessBoundedBlockingQueue.WaitNode wait = null;
        synchronized (pollLock) {
            // 在有阻塞请求或者队列为空时，阻塞等待
            if (canPollCount <= 0 || !pollQueue.isEmpty()) {
                wait = new com.cmg.java.阿里技术公众号.fairnessqueue.version8.FairnessBoundedBlockingQueue.WaitNode();
                pollQueue.enq(wait);
            } else {
                // ignore
            }
        }
        try {
            if (wait != null) {
                wait.doWait(123);
            }
            if (Thread.interrupted()) {
                throw new InterruptedException();
            }
        } catch (InterruptedException e) {
            // 传递消息
            pollQueue.doNotify();
            throw e;
        }

        // 以下不会检测线程中断状态
        synchronized (pollLock) {
            result = head.next.value;
            head.next.value = 0;
            // ignore head;
            head = head.next;
            canPollCount--;
        }

        synchronized (offerLock) {
            canOfferCount++;
            offerQueue.doNotify();
        }
        return result;
    }

    class WaitQueue {

        com.cmg.java.阿里技术公众号.fairnessqueue.version8.FairnessBoundedBlockingQueue.WaitNode head;
        com.cmg.java.阿里技术公众号.fairnessqueue.version8.FairnessBoundedBlockingQueue.WaitNode tail;

        WaitQueue() {
            head = new com.cmg.java.阿里技术公众号.fairnessqueue.version8.FairnessBoundedBlockingQueue.WaitNode();
            tail = head;
        }

        synchronized void doNotify() {
            for (; ; ) {
                com.cmg.java.阿里技术公众号.fairnessqueue.version8.FairnessBoundedBlockingQueue.WaitNode node = deq();
                if (node == null) {
                    break;
                } else if (node.doNotify()) {
                    // 此处确保NOTIFY成功
                    break;
                } else {
                    // ignore, and retry.
                }
            }
        }

        synchronized boolean isEmpty() {
            return head.next == null;
        }

        synchronized void enq(com.cmg.java.阿里技术公众号.fairnessqueue.version8.FairnessBoundedBlockingQueue.WaitNode node) {
            tail.next = node;
            tail = tail.next;
        }

        synchronized com.cmg.java.阿里技术公众号.fairnessqueue.version8.FairnessBoundedBlockingQueue.WaitNode deq() {
            if (head.next == null) {
                return null;
            }
            com.cmg.java.阿里技术公众号.fairnessqueue.version8.FairnessBoundedBlockingQueue.WaitNode res = head.next;
            head = head.next;
            if (head.next == null) {
                tail = head; // 为空，迁移tail节点
            }
            return res;
        }
    }

    class TimeoutException extends InterruptedException {
    }

    class WaitNode {
        boolean released;
        WaitNode next;

        WaitNode() {
            released = false;
            next = null;
        }

        synchronized void doWait(long milliSeconds) throws InterruptedException {
            try {
                long startTime = System.currentTimeMillis();
                long toWait = milliSeconds;
                for (; ; ) {
                    wait(toWait);
                    if (released) {
                        return;
                    }
                    long now = System.currentTimeMillis();
                    toWait = toWait - (now - startTime);
                    if (toWait <= 0) {
                        throw new TimeoutException();
                    }
                }
            } catch (InterruptedException e) {
                if (!released) {
                    released = true;
                    throw e;
                } else {
                    // 如果已经释放信号量，此处不抛出异常；但恢复中断状态
                    Thread.currentThread().interrupt();
                }
            }
        }

        synchronized boolean doNotify() {
            if (!released) {
                released = true;
                notify();
                return true;
            } else {
                return false;
            }
        }
    }

    // 省略 Node 的定义
    class Node {
        Object value;
        com.cmg.java.阿里技术公众号.fairnessqueue.version8.FairnessBoundedBlockingQueue.Node next;

        Node(Object obj) {
            this.value = obj;
            next = null;
        }
    }
}
