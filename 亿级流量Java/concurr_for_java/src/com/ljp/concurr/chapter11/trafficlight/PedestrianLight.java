package com.ljp.concurr.chapter11.trafficlight;

import com.sun.org.apache.regexp.internal.RE;

/**
 * 行人交通信号灯
 */
public class PedestrianLight implements Runnable {
    //因没有车辆刹车的耗时所需的过度阶段黄灯，所以仅包含绿红两种灯
    GreenLight greenLight = new GreenLight();
    RedLight redLight = new RedLight();

    // 分别定义绿黄红灯的默认保持时间，可以通过构造函数重新设定。
    int glKeepAliveTime = 30;
    int rlKeepAliveTime = 15;


    public PedestrianLight(int glKeepAliveTime, int rlKeepAliveTime){
        this.glKeepAliveTime = glKeepAliveTime;
        this.rlKeepAliveTime = rlKeepAliveTime;
    }

    @Override
    public void run() {
        //
        while (true) {
            // 路口01的行人通行方向为运行通行，则进行行人绿灯指示
            if (RunSimpleTL.crDirection.get("crossing_01_pedestrian") == true) {
                // 绿灯处理
                int glRunTime = 0;
                greenLight.setOffFlag(false);
                redLight.setOffFlag(true);

                while (glRunTime < glKeepAliveTime) {
                    System.out.println(Thread.currentThread().getName() + "绿灯");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    glRunTime++;
                }

                RunSimpleTL.crDirection.put("crossing_01_pedestrian", false);

            } else {
                // 红灯处理
                greenLight.setOffFlag(true);
                redLight.setOffFlag(false);

                int rlRunTime = 0;
                System.out.println(Thread.currentThread().getName() + "红灯");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                rlRunTime++;
            }
        }
    }

    public String getLightOnColor(){
        String rtnColor = "";
        if (greenLight.getOffFlag() == false){
            rtnColor = greenLight.color;
        }
        if (redLight.getOffFlag() == false){
            rtnColor = redLight.color;
        }
        return rtnColor;
    }

}
