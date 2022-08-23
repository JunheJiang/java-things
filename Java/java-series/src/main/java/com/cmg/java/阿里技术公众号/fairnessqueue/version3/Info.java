package com.cmg.java.阿里技术公众号.fairnessqueue.version3;

public class Info {
//    卫式方法
//
//    阻塞等待，可以通过简单的卫式方法来实现，此问题本质上可以抽象为：
//
//    任何一个方法都需要在满足一定条件下才可以执行；
//    执行方法前需要首先校验不变式，然后执行变更；
//    在执行完成后，校验是否满足后验不变式；
//
//    WHEN(condition) Object action(Object arg) {
//        checkPreCondition();
//        doAction(arg);
//        checkPostCondition();
//    }
//
//    此种抽象 Ada 在语言层面上实现。在 JAVA 中，借助 wait, notify, notifyAll 可以翻译为：
//
//    // 当前线程
//    synchronized Object action(Object arg) {
//        while(!condition) {
//            wait();
//        }
//        // 前置条件，不变式
//        checkPreCondition();
//        doAction();
//        // 后置条件，不变式
//        checkPostCondition();
//    }
//
//    // 其他线程
//    synchronized Object notifyAction(Object arg) {
//        notifyAll();
//    }
//
//    需要注意：
//
//    通常会采用 notifyAll 发送通知，而非 notify ；
//    因为如果当前线程收到 notify 通知后被中断，那么系统将一直等待下去。
//
//    如果使用了 notifyAll 那么卫式语句必须放在 while 循环中；
//    因为线程唤醒后，执行条件已经不满足，虽然当前线程持有互斥锁。
//
//    卫式条件的所有变量，有任何变更都需要发送 notifyAll 不然面临系统活性问题
}
