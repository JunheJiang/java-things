package com.ljp.concurr.chapter11.trafficlight;

import com.ljp.concurr.chapter07.CarPoolCountDownLatch;

import java.util.HashMap;
import java.util.Map;

public class RunSimpleTL {
    // 路口当前行人方向的全局存储，每一组key-value代表某一个路口的
    // 当前行人方向是否是允许通行。
    public static Map<String, Boolean> crDirection = new HashMap<String, Boolean>();

    public static void main(String[] args){
        CarSignalLight traffic01CarLight = new CarSignalLight(30, 5, 15);
        Thread trf01CarLightThread = new Thread(traffic01CarLight);
        trf01CarLightThread.setName("T01-CarSignalLight");

        PedestrianLight traffic01PedestrianLight = new PedestrianLight(15, 35);
        Thread trf01PedestrianLightThread = new Thread(traffic01PedestrianLight);
        trf01PedestrianLightThread.setName("T01-PedestrianLight");

        crDirection.put("crossing_01_pedestrian", true);

        trf01CarLightThread.start();
        trf01PedestrianLightThread.start();

        // 假设第8秒，18秒和48秒的时候有行人想过马路，
        // 我们来看看他们会看到怎样的信号灯：
        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("这一时刻看到该路口的交通信号灯是：行人信号灯信号是'" +
                traffic01PedestrianLight.getLightOnColor() + "', 而车辆行驶" +
                "交通信号灯信号是'" +
                traffic01CarLight.getLightOnColor() + "'");

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("这一时刻看到该路口的交通信号灯是：行人信号灯信号是'" +
                traffic01PedestrianLight.getLightOnColor() + "', 而车辆行驶" +
                "交通信号灯信号是'" +
                traffic01CarLight.getLightOnColor() + "'");

        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("这一时刻看到该路口的交通信号灯是：行人信号灯信号是'" +
                traffic01PedestrianLight.getLightOnColor() + "', 而车辆行驶" +
                "交通信号灯信号是'" +
                traffic01CarLight.getLightOnColor() + "'");

    }
}
