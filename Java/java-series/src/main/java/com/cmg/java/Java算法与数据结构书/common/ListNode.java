package com.cmg.java.Java算法与数据结构书.common;

import lombok.Data;

@Data
public class ListNode {

    public int data;
    public ListNode next;

    public ListNode(int data) {
        this.data = data;
    }
}
