package com.cmg.java.Java算法与数据结构书.chapter4.stack;

import com.cmg.java.Java算法与数据结构书.common.ListNode;

/**
 * 基于链表实现
 */
public class Q1_LLStack {

    private ListNode headNode;

    public Q1_LLStack() {
        this.headNode = null;
    }

    public void push(int data) {
        if (headNode == null) {
            headNode = new ListNode(data);
        } else if (headNode.getData() == null) {
            headNode.setData(data);
        } else {
            ListNode node = new ListNode();
            node.setNext(headNode);
            headNode = node;
        }
    }

    public Integer top() {
        if (headNode == null) return null;
        else return headNode.getData();
    }


    public Integer pop() {
        if (headNode == null) return null;
        else {
            int data = headNode.getData();
            headNode = headNode.getNext();
            return data;
        }
    }


    public boolean isEmpty() {
        return headNode == null;
    }

    public void deleteStack() {
        headNode = null;
    }

    /**
     * 递增 一次push
     * 倍增 一次
     */

}
