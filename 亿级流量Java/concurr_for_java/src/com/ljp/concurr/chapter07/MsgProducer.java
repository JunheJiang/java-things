package com.ljp.concurr.chapter07;

public class MsgProducer implements Runnable {

    private String tmpMessage = "";

    @Override
    public void run() {
        for(int i=1; i<=1000; i++){
            tmpMessage = "我在数数，我数到了第" + i + "个数,你能收到吗？";
            //这里调用RunAsynchDemo中的一个list，作为简单的消息队列，将临时信息放入。
            try {
                // 使用并发工具信号量来控制模拟MQ的读写
                RunAsynchDemo.mqSemaphore.acquire();
                RunAsynchDemo.simpleMQ.add(tmpMessage);
                RunAsynchDemo.mqSemaphore.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            try {
                //睡300毫秒后继续。
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
