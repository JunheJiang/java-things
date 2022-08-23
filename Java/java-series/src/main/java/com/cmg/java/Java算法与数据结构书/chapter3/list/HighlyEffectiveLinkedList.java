//package com.cmg.java.algorithmBook.list;
//
//import com.cmg.java.algorithmBook.list.questions.ReferenceToAddress;
//
//import java.io.Serializable;
//
//public class HighlyEffectiveLinkedList<T> implements Serializable {
//
//    // optimizing traverse etc stateful actions
//    private EffectiveCircularLinkedNode headNode;
//    private EffectiveCircularLinkedNode secondNode;
//    private EffectiveCircularLinkedNode empty = null;
//
//    public HighlyEffectiveLinkedList(T data1, T data2) throws Exception {
//        headNode = new EffectiveCircularLinkedNode(data1);
//        secondNode = new EffectiveCircularLinkedNode(data2);
//        long diff = ReferenceToAddress.addressOf(empty) ^ ReferenceToAddress.addressOf(secondNode);
//        System.out.println(diff);
////        headNode.setDiff();
//    }
//
//    public static void main(String[] args) throws Exception {
//        HighlyEffectiveLinkedList highlyEffectiveLinkedList = new HighlyEffectiveLinkedList(1, 2);
//    }
//
//}
