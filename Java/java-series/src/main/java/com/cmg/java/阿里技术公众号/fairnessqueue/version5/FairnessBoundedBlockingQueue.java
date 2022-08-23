package com.cmg.java.阿里技术公众号.fairnessqueue.version5;

import com.cmg.java.阿里技术公众号.fairnessqueue.version3.Queue;

/**
 * 以通过状态追踪，解除读与读之间和写与写之间的竞争问题
 *
 * 通过 waitOfferCount 和 waitPollCount 的状态追踪解决 读写内部的竞争问题；
 * 当队列变更时，根据追踪的状态，决定是否派发消息，触发线程阻塞状态解除；
 *
 * 但，上述的实现在某些场景下会运行失败，面临活性问题，考虑
 *
 * 情况一：
 *
 * 初始状态队列为空 线程 A 执行出队动作，被阻塞在 pollLock , 此时 waitPollCount==1；
 *
 * 此时线程 A 在执行 wait 时被中断，抛出异常， waitPollCount==1 并未被重置；
 *
 * 阻塞队列为空，但 waitPollCount==1 类状态异常；
 *
 * 情况二：
 *
 * 初始状态队列为空 线程 A B 执行出队动作，被阻塞在 pollLock , 此时 waitPollCount==2；
 *
 * 线程 C 执行入队动作，可以立即执行，执行完成后，触发 pollLock 解除一个线程等待 notify；
 *
 * 触发的线程在 JVM 实现中是随机的，假设线程 A 被解除阻塞；
 *
 * 假设线程 A 在阻塞过程中已被中断，阻塞解除后 JVM 检查 interrupted 状态，抛出 InterruptedException 异常；
 *
 * 此时队列中有一个元素，但线程 A 仍阻塞在 pollLock 中，且一直阻塞下去；
 *
 * 以上为解除阻塞消息丢失的例子，问题的根源在与异常处理。
 */
class FairnessBoundedBlockingQueue implements Queue {
    // 容量
    protected final int capacity;

    // 头指针，empty: head.next == tail == null
    protected Node head;

    // 尾指针
    protected Node tail;

    // guard: canPollCount, head
    protected final Object pollLock = new Object();
    protected int canPollCount;
    protected int waitPollCount;

    // guard: canOfferCount, tail
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
        synchronized (offerLock) {
            while (canOfferCount <= 0) {
                waitOfferCount++;
                offerLock.wait();
                waitOfferCount--;
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
        Object result;
        synchronized (pollLock) {
            while (canPollCount <= 0) {
                waitPollCount++;
                pollLock.wait();
                waitPollCount--;
            }

            result = head.next.value;
            head.next.value = null;
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
        com.cmg.java.阿里技术公众号.fairnessqueue.version5.FairnessBoundedBlockingQueue.Node next;

        Node(Object obj) {
            this.value = obj;
            next = null;
        }
    }
}