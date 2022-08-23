package com.cmg.java.Java算法与数据结构书.chapter3.list;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

public class DDLinkedList implements Serializable {

    private DDLinkedNode headNode;

    public DDLinkedList(int data) {
        headNode = new DDLinkedNode(data);
    }

    @Getter
    @Setter
    public class DDLinkedNode implements Serializable {
        private int data;
        private DDLinkedNode next;
        private DDLinkedNode pre;

        public DDLinkedNode(int data) {
            this.data = data;
        }
    }

    int length() {
        int length = 0;
        if (null == this.headNode)
            return length;
        DDLinkedNode currentNode = this.headNode;
        while (null != currentNode) {
            length++;
            currentNode = currentNode.getNext();
        }
        return length;
    }

    DDLinkedNode insertAt(int data, int position) {
        DDLinkedNode insertNode = new DDLinkedNode(data);
        if (null == this.headNode) {
            insertNode.setNext(null);
            insertNode.setPre(null);
            return insertNode;
        }
        int size = length();
        if (position > size + 1 || position < 1) {
            return this.headNode;
        }
        if (position == 1) {
            insertNode.setPre(null);
            insertNode.setNext(this.headNode);
            return insertNode;
        } else {
            DDLinkedNode previousNode = this.headNode;
            int count = 1;
            while (count < position - 1) {
                previousNode = previousNode.getNext();
                count++;
            }
            DDLinkedNode currentNode = previousNode.getNext();
            insertNode.setNext(currentNode);
            if (null != currentNode) {
                currentNode.setPre(insertNode);
            }
            previousNode.setNext(insertNode);
            return this.headNode;
        }
    }

    DDLinkedNode deleteAt(int position) {
        int size = length();
        if (position > size || position < 1) {
            return this.headNode;
        }
        if (position == 1) {
            DDLinkedNode currentNode = this.headNode.getNext();
            headNode = null;
            currentNode.setPre(null);
            return currentNode;
        } else {
            DDLinkedNode previousNode = this.headNode;
            int count = 1;
            while (count < position - 1) {
                previousNode = previousNode.getNext();
                count++;
            }
            DDLinkedNode currentNode = previousNode.getNext();
            DDLinkedNode laterNode = currentNode.getNext();
            previousNode.setNext(laterNode);
            if (null != laterNode) {
                laterNode.setPre(currentNode);
            }
            currentNode = null;
        }
        return this.headNode;
    }


}
