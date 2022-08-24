package com.yzxie.java.demo.charter6.part_6_3;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author xieyizun
 * @date 20/5/2019 21:36
 * @description:
 */
public class ConcurrentLinkedQueueDemo {
    public static void main(String[] args) {
        ConcurrentLinkedQueue<String> soldTickets = new ConcurrentLinkedQueue<>();

        new Thread(new Buyer(soldTickets)).start();
        new Thread(new StatisticTask(soldTickets)).start();
    }

    /**
     * 购票线程
     */
    static class Buyer implements Runnable {
        private ConcurrentLinkedQueue<String> soldTickets;

        public Buyer(ConcurrentLinkedQueue<String> soldTickets) {
            this.soldTickets = soldTickets;
        }

        @Override
        public void run() {
            int i = 0;

            while (true) {
                // 每隔一秒填充一个数据到队列中
                soldTickets.add(new String("ticket-" + i++));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 后台统计线程
     */
    static class StatisticTask implements Runnable {
        private ConcurrentLinkedQueue<String> soldTickets;

        public StatisticTask(ConcurrentLinkedQueue<String> soldTickets) {
            this.soldTickets = soldTickets;
        }

        @Override
        public void run() {
            while (true) {
                // 每隔5秒打印一次当前队列中的数据
                System.out.println(soldTickets);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
