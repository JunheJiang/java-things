package com.ljp.concurr.chapter07;

import java.util.List;

public class MsgConsumer implements Runnable{

    @Override
    public void run() {
        while (true){
            if (RunAsynchDemo.simpleMQ.size() > 0){
                try {
                    // 使用并发工具信号量来控制模拟MQ的读写
                    RunAsynchDemo.mqSemaphore.acquire();
                    for (String tmpReceiverMsg : RunAsynchDemo.simpleMQ){
                        System.out.println("我收到了远方传来的信息：" + tmpReceiverMsg);
                    }
                    // 消费完简单消息队列中的内容，则清除它们
                    RunAsynchDemo.simpleMQ.clear();
                    RunAsynchDemo.mqSemaphore.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

            try {
                //这里休息800毫秒再继续
                Thread.sleep(800);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
