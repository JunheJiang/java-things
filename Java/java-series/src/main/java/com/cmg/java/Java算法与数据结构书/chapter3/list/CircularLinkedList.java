package com.cmg.java.Java算法与数据结构书.chapter3.list;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class CircularLinkedList implements Serializable {
    private CircularLinkedNode headNode;

    public CircularLinkedList(int data) {
        headNode = new CircularLinkedNode(data);
    }


    int length() {
        int length = 0;
        CircularLinkedNode currentNode = this.headNode;
        while (null != currentNode) {
            length++;
            currentNode = currentNode.getNext();
            if (currentNode == this.headNode)
                break;
        }
        return length;
    }

    void insertAtHead(int data) {
        CircularLinkedNode insertNode = new CircularLinkedNode(data);
        CircularLinkedNode currentNode = this.headNode;
        //direct to itself
        while (currentNode.getNext() != this.headNode) {
            //interesting
            currentNode = currentNode.getNext();
        }
        insertNode.setNext(this.headNode);
        currentNode.setNext(insertNode);
        this.headNode = insertNode;
    }

    void insertAtEnd(int data) {
        CircularLinkedNode insertNode = new CircularLinkedNode(data);
        CircularLinkedNode currentNode = this.headNode;
        //direct to itself
        while (currentNode.getNext() != this.headNode) {
            //interesting bug occurred as book follows
            currentNode = currentNode.getNext();
        }
        currentNode.setNext(insertNode);
        insertNode.setNext(this.headNode);
        currentNode = this.headNode;
        System.out.println(currentNode.getData());
        while (currentNode.getNext() != this.headNode) {
            //interesting bug occurred as book follows
            currentNode = currentNode.getNext();
            System.out.println(currentNode.getData());
        }
    }

    public static void main(String[] args) {
        CircularLinkedList linkedList = new CircularLinkedList(1);
        CircularLinkedNode node = new CircularLinkedNode(2);
        linkedList.getHeadNode().setNext(node);
        node.setNext(linkedList.getHeadNode());
        node.setPre(linkedList.getHeadNode());
        linkedList.insertAtEnd(3);
    }

}
