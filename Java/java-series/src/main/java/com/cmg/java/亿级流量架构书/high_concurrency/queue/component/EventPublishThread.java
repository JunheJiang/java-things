package com.cmg.java.亿级流量架构书.high_concurrency.queue.component;

import com.lmax.disruptor.EventTranslatorTwoArg;
import com.lmax.disruptor.RingBuffer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EventPublishThread extends Thread {
    boolean running = true;
    String eventType;
    EventQueue eventQueue;
    RingBuffer<Event> ringBuffer;

    public EventPublishThread(String eventType,
                              EventQueue eventQueue,
                              RingBuffer ringBuffer) {
        this.eventType = eventType;
        this.eventQueue = eventQueue;
        this.ringBuffer = ringBuffer;
    }

    static EventTranslatorTwoArg<Event, String, String> EVENT_TRANSLATOR = new EventTranslatorTwoArg<Event, String, String>() {
        @Override
        public void translateTo(Event event, long sequence, String key, String eventType) {
            event.setEventType(eventType);
            event.setKey(key);
        }
    };

    @Override
    public void run() {
        while (running) {
            String nextKey = null;
            try {
                if (nextKey == null) {
                    nextKey = eventQueue.next();
                }
                if (nextKey != null) {
                    ringBuffer.publishEvent(EVENT_TRANSLATOR, nextKey, eventType);
                }
            } catch (Exception e) {

            }
        }
    }

    public void shutDown() {
        this.running = false;
    }
}
