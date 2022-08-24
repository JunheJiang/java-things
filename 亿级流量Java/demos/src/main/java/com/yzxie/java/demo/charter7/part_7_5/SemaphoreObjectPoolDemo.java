package com.yzxie.java.demo.charter7.part_7_5;

import java.util.concurrent.Semaphore;

/**
 * Author: xieyizun
 * Version: 1.0
 * Date: 2019-09-22
 * Description:
 **/
public class SemaphoreObjectPoolDemo {

    private static final int MAX_AVAILABLE = 100;

    // 对象池可用的对象的数量定义
    private final Semaphore available = new Semaphore(MAX_AVAILABLE, true);

    // 从对象池获取一个可用对象
    public Object getItem() throws InterruptedException {
        available.acquire();
        return getNextAvailableItem();
    }

    // 往对象池添加一个可用对象
    public void putItem(Object x) {
        if (markAsUnused(x))
            available.release();
    }

    // 对象池初始化，类型为一个对象数组
    protected Object[] items = new Object[MAX_AVAILABLE];

    // 记录已经被使用的对象
    protected boolean[] used = new boolean[MAX_AVAILABLE];

    // 获取一个可用的对象，使用synchronized关键字加锁
    protected synchronized Object getNextAvailableItem() {
        for (int i = 0; i < MAX_AVAILABLE; ++i) {
            if (!used[i]) {
                // 对应位置设置为true，表示该位置的对象已经被使用了
                used[i] = true;
                return items[i];
            }
        }
        return null;
    }

    protected synchronized boolean markAsUnused(Object item) {
        for (int i = 0; i < MAX_AVAILABLE; ++i) {
            if (item == items[i]) {
                if (used[i]) {
                    // 对应位置设置为false，表示该对象没有被使用
                    used[i] = false;
                    return true;
                } else
                    return false;
            }
        }
        return false;
    }
}
