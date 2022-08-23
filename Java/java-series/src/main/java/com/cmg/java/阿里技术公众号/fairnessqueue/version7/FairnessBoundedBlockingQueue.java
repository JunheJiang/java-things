package com.cmg.java.阿里技术公众号.fairnessqueue.version7;

import com.cmg.java.阿里技术公众号.fairnessqueue.version3.Queue;

/**
 * 公平性的问题的解决需要将状态变量的追踪转换为：请求监视器追踪。
 * <p>
 * 每个请求对应一个监视器；
 * 通过内部维护一个 FIFO 队列，实现公平性；
 * 在队列状态变更时，释放队列中的监视器；
 * <p>
 * boolean needToWait;
 * synchronized(this) {
 * needToWait = calculateNeedToWait();
 * if (needToWait) {
 * enqueue(monitor); // 请求对应的monitor
 * }
 * }
 * if (needToWait) {
 * monitor.doWait();
 * }
 * <p>
 * monitor.doWait() 需要在 this 的卫式语句之外，因为如果在内部， monitor.doWait 并不会释放 this锁；
 * <p>
 * calculateNeedToWait() 需要在 this 的守卫之内完成，避免同步问题；
 * <p>
 * 需要考虑中断异常的问题；
 * <p>
 * 核心是替换状态追踪变量为同步节点， WaitNode；
 * <p>
 * <p>
 * WaitNode 通过简单的同步队列组织实现 FIFO 协议，每个线程等待各自的 WaitNode 监视器；
 * <p>
 * WaitNode 内部维持 released 状态，标识线程阻塞状态是否被释放，主要是为了处理中断的问题；
 * <p>
 * WaitQueue 本身是全同步的，由于已解决了读写竞争已经读写内部竞争的问题， WaitQueue 同步并不会造成问题；
 * <p>
 * WaitQueue 是无界队列，是一个潜在的问题；但由于其只做同步的追踪，而且追踪的通常是线程，通常并不是问题；
 * <p>
 * 最终的公平有界队列实现，无论是入队还是出队，首先卫式语句判定是否需要入队等待，如果入队等待，通过公平性协议等待;
 * 当信号释放时，借助读写锁同步更新队列；最后同样借助读写锁，触发队列更新消息；
 */
class FairnessBoundedBlockingQueue implements Queue {
    // 容量
    protected final int capacity;

    // 头指针，empty: head.next == tail == null
    protected Node head;

    // 尾指针
    protected Node tail;

    // guard: canPollCount, head, pollQueue
    protected final Object pollLock = new Object();
    protected int canPollCount;

    // guard: canOfferCount, tail, offerQueue
    protected final Object offerLock = new Object();
    protected int canOfferCount;

    protected final WaitQueue pollQueue = new WaitQueue();
    protected final WaitQueue offerQueue = new WaitQueue();

    public FairnessBoundedBlockingQueue(int capacity) {
        this.capacity = capacity;
        this.canOfferCount = capacity;
        this.canPollCount = 0;
        this.head = new Node(null);
        this.tail = head;
    }

    // 如果队列已满，通过返回值标识
    public boolean offer(Object obj) throws InterruptedException {
        if (Thread.interrupted()) {
            throw new InterruptedException(); // 线程已中断，直接退出即可，防止中断线程竞争锁
        }
        WaitNode wait = null;
        synchronized (offerLock) {
            // 在有阻塞请求或者队列为空时，阻塞等待
            if (canOfferCount <= 0 || !offerQueue.isEmpty()) {
                wait = new WaitNode();
                offerQueue.enq(wait);
            } else {
                // continue.
            }
        }

        try {
            if (wait != null) {
                wait.doWait();
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
            Node node = new Node(obj);
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
        WaitNode wait = null;
        synchronized (pollLock) {
            // 在有阻塞请求或者队列为空时，阻塞等待
            if (canPollCount <= 0 || !pollQueue.isEmpty()) {
                wait = new WaitNode();
                pollQueue.enq(wait);
            } else {
                // ignore
            }
        }

        try {
            if (wait != null) {
                wait.doWait();
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

        WaitNode head;
        WaitNode tail;

        WaitQueue() {
            head = new WaitNode();
            tail = head;
        }

        synchronized void doNotify() {
            for (; ; ) {
                WaitNode node = deq();
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

        synchronized void enq(WaitNode node) {
            tail.next = node;
            tail = tail.next;
        }

        synchronized WaitNode deq() {
            if (head.next == null) {
                return null;
            }
            WaitNode res = head.next;
            head = head.next;
            if (head.next == null) {
                tail = head; // 为空，迁移tail节点
            }
            return res;
        }
    }

    class WaitNode {
        boolean released;
        WaitNode next;

        WaitNode() {
            released = false;
            next = null;
        }

        synchronized void doWait() throws InterruptedException {
            try {
                while (!released) {
                    wait();
                }
            } catch (InterruptedException e) {
                if (!released) {
                    released = true;
                    throw e;
                } else {
                    // 如果是NOTIFY之后收到中断的信号，不能抛出异常；需要做RELAY处理
                    Thread.currentThread().interrupt();
                }
            }
        }

        synchronized boolean doNotify() {
            if (!released) {
                released = true;
                notify();
                // 明确释放了一个线程，返回true
                return true;
            } else {
                // 没有释放新的线程，返回false
                return false;
            }
        }
    }

    // 省略 Node 的定义
    class Node {
        Object value;
        com.cmg.java.阿里技术公众号.fairnessqueue.version7.FairnessBoundedBlockingQueue.Node next;

        Node(Object obj) {
            this.value = obj;
            next = null;
        }
    }
}
