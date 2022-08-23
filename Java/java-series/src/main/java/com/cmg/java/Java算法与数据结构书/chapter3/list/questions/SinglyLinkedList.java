package com.cmg.java.Java算法与数据结构书.chapter3.list.questions;

import com.cmg.java.Java算法与数据结构书.chapter3.list.LinkedNode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * singly linked list of integer
 */
@Getter
@Setter
public class SinglyLinkedList implements Serializable {

    private LinkedNode headNode;

    public SinglyLinkedList(int data) {
        headNode = new LinkedNode(data);
    }


    public int length() {
        int length = 0;
        if (null == this.headNode)
            return length;
        LinkedNode currentNode = this.headNode;
        while (null != currentNode) {
            length++;
            currentNode = currentNode.getNext();
        }
        return length;
    }

    /**
     * default of adding at the end of list
     *
     * @param data
     */
    void add(int data) {
        LinkedNode addedNode = new LinkedNode(data);
        addedNode.setNext(null);
        LinkedNode previousNode = this.headNode;
        LinkedNode temp = null;
        while (null != previousNode) {
            temp = previousNode;
            previousNode = previousNode.getNext();
        }
        temp.setNext(addedNode);
    }

    /**
     * add in specified position
     *
     * @param data
     * @param position
     */
    void addAt(int data, int position) {
        LinkedNode addedNode = new LinkedNode(data);
        //add at front of list
        if (position == 1) {
            addedNode.setNext(this.headNode);
            this.headNode.setNext(null);
            return;
        } else {
            int count = 1;
            LinkedNode previousNode = this.headNode;
            while (count < position - 1) {
                previousNode = previousNode.getNext();
                count++;
            }
            LinkedNode currentNode = previousNode.getNext();
            addedNode.setNext(currentNode);
            previousNode.setNext(addedNode);
        }
    }


    /**
     * add and reuturn the node
     *
     * @param data
     * @param position
     * @return
     */
    LinkedNode add(int data, int position) {
        LinkedNode insertNode = new LinkedNode(data);
        if (null == this.headNode) {
            insertNode.setNext(null);
            return insertNode;
        }
        int size = length();
        if (position > size + 1 || position <= 1)
            return this.headNode;
        LinkedNode previousNode = this.headNode;
        int count = 1;
        while (count < position - 1) {
            previousNode = previousNode.getNext();
            count++;
        }
        LinkedNode currentNode = previousNode.getNext();
        insertNode.setNext(currentNode);
        previousNode.setNext(insertNode);
        return this.headNode;
    }

    /**
     * default of deleting the end data of list
     */
    void remove() {
        LinkedNode previousNode = this.headNode;
        LinkedNode temp = null;
        while (null != previousNode) {
            temp = previousNode;
            previousNode = previousNode.getNext();
        }
        temp.setNext(null);
    }

    void remove(int position) {
        //to do
    }

    /**
     * empty itself
     */
    void delToEmpty() {
        LinkedNode temp, iterator = this.headNode;
        while (null != iterator) {
            temp = iterator.getNext();
            iterator = null;
            iterator = temp;
        }
    }

    LinkedNode delete(int data, int position) {
        int size = length();
        if (position > size || position <= 1) {
            return this.headNode;
        }
        LinkedNode previousNode = this.headNode;
        int count = 1;
        while (count < position) {
            previousNode = previousNode.getNext();
            count++;
        }
        LinkedNode currentNode = previousNode.getNext();
        previousNode.setNext(currentNode.getNext());
        currentNode = null;
        return this.headNode;
    }

}
