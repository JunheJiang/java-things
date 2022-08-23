package com.cmg.java.阿里技术公众号.fairnessqueue.version3;

/**
 * 实现了阻塞等待，但也引入了更大的性能问题
 * <p>
 * 入队和出队动作阻塞等待同一把锁，恶性竞争；
 * <p>
 * 当队列变更时，所有阻塞线程被唤醒，大量的线程上下文切换，竞争同步锁，最终可能只有一个线程能执行；
 * <p>
 * 需要注意的点：
 * <p>
 * 阻塞等待 wait 会抛出中断异常。关于异常的问题下文会处理；
 * 接口需要支持抛出中断异常；
 * 队里变更需要 notifyAll 避免线程中断或异常，丢失消息
 */
class FairnessBoundedBlockingQueue implements Queue {
    // 当前大小
    protected int size;

    // 容量
    protected final int capacity;

    // 头指针，empty: head.next == tail == null
    protected Node head;

    // 尾指针
    protected Node tail;

    public FairnessBoundedBlockingQueue(int capacity) {
        this.capacity = capacity;
        this.head = new Node(null);
        this.tail = head;
        this.size = 0;
    }

    // 如果队列已满，通过返回值标识
    public synchronized boolean offer(Object obj) throws InterruptedException {
        while (size >= capacity) {
            wait();
        }
        Node node = new Node(obj);
        tail.next = node;
        tail = node;
        ++size;
        notifyAll(); // 可以出队
        return true;
    }

    // 如果队列为空，阻塞等待
    public synchronized Object poll() throws InterruptedException {
        while (head.next == null) {
            wait();
        }
        Object result = head.next.value;
        head.next.value = null;
        head = head.next; // 丢弃头结点
        --size;
        notifyAll(); // 可以入队
        return result;
    }

    // 省略 Node 的定义
    class Node {
        Object value;
        com.cmg.java.阿里技术公众号.fairnessqueue.version3.FairnessBoundedBlockingQueue.Node next;

        Node(Object obj) {
            this.value = obj;
            next = null;
        }
    }
}