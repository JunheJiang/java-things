package com.ljp.concurr.chapter07;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class RunAsynchDemo {

    // 这里我们将使用ArrayList来定义一个简单的消息队列simpleMQ，
    // 用来模拟商用级别的MQ，譬如Kafka、RabbitMQ等。
    public static List<String> simpleMQ = new ArrayList<String>();
    // 这个是MQ使用中的信号量标识，确保一个时刻内只有一个线程可以对其读写
    // 如果采用商业级别的MQ，则内部已经包含严密的机制处理这类问题。
    public static Semaphore mqSemaphore = new Semaphore(1);

    public static void main(String[] args){
        Thread msgProducer = new Thread(new MsgProducer());
        Thread msgConsumer = new Thread(new MsgConsumer());
        msgProducer.start();
        msgConsumer.start();
    }
}
