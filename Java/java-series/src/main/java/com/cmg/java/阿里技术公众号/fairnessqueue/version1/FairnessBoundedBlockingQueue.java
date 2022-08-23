package com.cmg.java.阿里技术公众号.fairnessqueue.version1;

import com.cmg.java.阿里技术公众号.fairnessqueue.Queue;

/**
 * 基础版本 无并发情况
 * <p>
 * <p>
 * 将公平的定义限定为 FIFO ，也就是先阻塞等待的请求，先解除等待；
 * 并不保证解除等待后执行 Action 的先后顺序；
 * 确保队列的大小始终不超过设定的容量；但阻塞等待的请求数不做限制；
 */
public class FairnessBoundedBlockingQueue implements Queue {

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
    public boolean offer(Object obj) {
        if (size < capacity) {
            Node node = new Node(obj);
            tail.next = node;
            tail = node;
            ++size;
            return true;
        }
        return false;
    }

    // 如果队列为空，head.next == null；返回空元素
    public Object poll() {
        if (head.next != null) {
            Object result = head.next.value;
            head.next.value = null;
            head = head.next; // 丢弃头结点
            --size;
            return result;
        }
        return null;
    }

    class Node {
        Object value;
        Node next;

        Node(Object obj) {
            this.value = obj;
            next = null;
        }
    }
}
