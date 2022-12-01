package com.cmg.java.Java算法与数据结构书.common;

import lombok.Data;

@Data
public class ListNode {

    public Integer data;
    public ListNode next;
    private ListNode random;

    public ListNode(int data) {
        this.data = data;
    }
    public ListNode() {
    }
}
