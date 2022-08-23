package com.cmg.java.亿级流量架构书.high_concurrency.queue.component;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

/**
 * 队列事件处理线程
 */
public class EventWorker {
    int threadPoolSize = 256;
    int ringBufferSize = 4096;
    Map<EventQueue, EventHandler> eventHandlerMap;
    Map<String, EventQueue> eventQueueMap;
    Disruptor disruptor;
    RingBuffer ringBuffer;
    List<EventPublishThread> eventPublishThreads;

    public void setEventHandlerMap(Map<EventQueue, EventHandler> eventHandlerMap) {
        this.eventHandlerMap = eventHandlerMap;
        for (Map.Entry<EventQueue, EventHandler> entry : eventHandlerMap.entrySet()) {
            EventQueue queue = entry.getKey();
            this.eventQueueMap.put(queue.getQueueName(), queue);
        }
    }

    public void init() throws Exception {
        disruptor = new Disruptor<Event>(
                new EventFactory() {
                    @Override
                    public Event newInstance() {
                        return null;
                    }
                },
                ringBufferSize,
                Executors.newFixedThreadPool(threadPoolSize),
                ProducerType.MULTI,
                new BlockingWaitStrategy()
        );
        ringBuffer = disruptor.getRingBuffer();

        disruptor.handleExceptionsWith(new ExceptionHandler() {
            @Override
            public void handleEventException(Throwable throwable, long l, Object o) {

            }

            @Override
            public void handleOnStartException(Throwable throwable) {

            }

            @Override
            public void handleOnShutdownException(Throwable throwable) {

            }
        });

        WorkHandler<Event> workHandler = new WorkHandler<Event>() {
            @Override
            public void onEvent(Event event) throws Exception {
                String type = event.getEventType();
                EventQueue queue = eventQueueMap.get(type);
                EventHandler eventHandler = eventHandlerMap.get(queue);
                eventHandler.onEvent(event.getKey(), type, queue);
            }
        };

        WorkHandler[] workHandlers = new WorkHandler[threadPoolSize];
        for (int i = 0; i < threadPoolSize; i++) {
            workHandlers[i] = workHandler;
        }

        disruptor.handleEventsWithWorkerPool(workHandlers);
        disruptor.start();

        //每个类型队列创建一个发布者
        for (Map.Entry<String, EventQueue> eventQueueEntry :
                eventQueueMap.entrySet()) {
            String eventType = eventQueueEntry.getKey();
            EventQueue eventQueue = eventQueueEntry.getValue();
            EventPublishThread thread =
                    new EventPublishThread(eventType, eventQueue, ringBuffer);
            eventPublishThreads.add(thread);
            thread.start();
        }
    }

    public void stop() {
        for (EventPublishThread thread : eventPublishThreads) {
            thread.shutDown();
        }
        disruptor.shutdown();
    }
}
