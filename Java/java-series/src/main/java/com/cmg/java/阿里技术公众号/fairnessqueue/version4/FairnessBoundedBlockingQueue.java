package com.cmg.java.阿里技术公众号.fairnessqueue.version4;

import com.cmg.java.阿里技术公众号.fairnessqueue.version3.Queue;

/**
 * 锁拆分来解决，即：定义两把锁，读锁和写锁；读写分离
 *
 * 确保通过入队锁和出队锁，分别保证入队和出队的原子性；
 * 出队动作，通过特别的实现，确保出队只会变更 head ，避免获取 offerLock；
 * 通过 offerLock.notifyAll 和 pollLock.notifyAll 解决读写竞争的问题；
 * <p>
 * 但上述实现还有未解决的问题：
 * <p>
 * 当有多个入队线程等待时，一次出队的动作会触发所有入队线程竞争，大量的线程上下文切换，最终只有一个线程能执行。
 * <p>
 * 即，还有 读与读 和 写与写 之间的竞争问题。
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

    // guard: canOfferCount, tail
    protected final Object offerLock = new Object();
    protected int canOfferCount;

    public FairnessBoundedBlockingQueue(int capacity) {
        this.capacity = capacity;
        this.canPollCount = 0;
        this.canOfferCount = capacity;
        this.head = new Node(null);
        this.tail = head;
    }

    // 如果队列已满，通过返回值标识
    public boolean offer(Object obj) throws InterruptedException {
        synchronized (offerLock) {
            while (canOfferCount <= 0) {
                offerLock.wait();
            }
            Node node = new Node(obj);
            tail.next = node;
            tail = node;
            canOfferCount--;
        }
        synchronized (pollLock) {
            ++canPollCount;
            pollLock.notifyAll();
        }
        return true;
    }

    // 如果队列为空，阻塞等待
    public Object poll() throws InterruptedException {
        Object result = null;
        synchronized (pollLock) {
            while (canPollCount <= 0) {
                pollLock.wait();
            }

            result = head.next.value;
            head.next.value = null;
            head = head.next;
            canPollCount--;
        }
        synchronized (offerLock) {
            canOfferCount++;
            offerLock.notifyAll();
        }
        return result;
    }

    class Node {
        Object value;
        com.cmg.java.阿里技术公众号.fairnessqueue.version4.FairnessBoundedBlockingQueue.Node next;

        Node(Object obj) {
            this.value = obj;
            next = null;
        }
    }

}