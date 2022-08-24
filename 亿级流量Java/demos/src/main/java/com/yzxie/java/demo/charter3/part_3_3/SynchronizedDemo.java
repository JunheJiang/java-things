package com.yzxie.java.demo.charter3.part_3_3;

/**
 * @author xieyizun
 * @date 16/6/2019 15:09
 * @description:
 */
public class SynchronizedDemo {
    // synchronized关键字用在类的静态方法上
    // 使用SynchronizedDemo类对象自身作为监视器对象
    public static synchronized void testStaticMethod() {

    }

    // synchronized关键字用在类的成员方法上，
    // 使用SynchronizedDemo的对象实例作为监视器对象
    public synchronized void testMemberMethod() {

    }

    // 代码块的监视器对象
    private Object monitor = new Object();
    public void testCodeBlock() {
        synchronized (monitor) {

        }
    }
}
