package com.ljp.concurr.chapter03;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class FutureCatchExceptionDemo {

    public static void main(String[] args){
        Callable<String> getSubString = new SubStringCallable();
        FutureTask<String> stringFutureTask = new FutureTask<String>(getSubString);
        Thread subStringThread = new Thread(stringFutureTask);
        subStringThread.start();
        try {
            System.out.println(stringFutureTask.get());
        } catch (Exception e) {
            System.out.println("返回子字符串失败了，应该是截取下标越界了吧？");
        }

    }
}

class SubStringCallable implements Callable<String> {

    @Override
    public String call() throws Exception {
        String fullInfoString = "我是AAA字符串，据说我要被截取一小段，然后返回...";
        // 这里的子字符串startIndex，endIndex，可以自己设置多几次不同的值，
        // 这样做的目的是为了测试当下标越界的时候，
        // callable线程是否能抛出异常以及主线程是否可以获取到异常
        String tmpSubString = fullInfoString.substring(15, 50);
        return tmpSubString;
    }
}
