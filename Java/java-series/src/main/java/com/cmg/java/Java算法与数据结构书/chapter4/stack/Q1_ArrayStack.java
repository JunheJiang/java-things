package com.cmg.java.Java算法与数据结构书.chapter4.stack;

/**
 * 基于简单数组实现栈
 * <p>
 * 长度限制
 * 指定异常
 */
public class Q1_ArrayStack {

    private int top;
    private int capacity;
    private int[] array;

    public Q1_ArrayStack() {
        capacity = 1;
        top = -1;
        array = new int[capacity];
    }

    public boolean isEmpty() {
        return top == -1;
    }

    public boolean isStackFull() {
        return top == capacity - 1;
    }

    public void push(int data) {
        if (isStackFull()) return;
        else array[++top] = data;
    }

    public int pop() {
        if (isStackFull()) return 0;
        else return array[top--];
    }

    public void deleteStack() {
        top = -1;
    }
}
