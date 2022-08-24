package com.ljp.concurr.chapter11.trafficlight;

/**
 * 行车交通信号灯
 */
public class CarSignalLight implements Runnable {
    // 分别定义绿黄红灯的默认保持时间，可以通过构造函数重新设定。
    int glKeepAliveTime = 30;
    int ylKeepAliveTime = 5;
    int rlKeepAliveTime = 15;

    //包含了绿、黄、红三种灯
    GreenLight greenLight = new GreenLight();
    YellowLight yellowLight = new YellowLight();
    RedLight redLight = new RedLight();

    public CarSignalLight(int glKeepAliveTime, int ylKeepAliveTime, int rlKeepAliveTime){
        this.glKeepAliveTime = glKeepAliveTime;
        this.ylKeepAliveTime = ylKeepAliveTime;
        this.rlKeepAliveTime = rlKeepAliveTime;
    }

    @Override
    public void run() {
        //
        while (true) {
            // 路口01的行人通行标志为不允许通行时，则车辆执行通行
            if (RunSimpleTL.crDirection.get("crossing_01_pedestrian") == false) {
                // 绿灯处理
                int glRunTime = 0;
                greenLight.setOffFlag(false);
                yellowLight.setOffFlag(true);
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

                // 黄灯处理
                greenLight.setOffFlag(true);
                yellowLight.setOffFlag(false);
                redLight.setOffFlag(true);

                int ylRunTime = 0;
                while (ylRunTime < ylKeepAliveTime) {
                    System.out.println(Thread.currentThread().getName() + "黄灯");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    ylRunTime++;
                }

                RunSimpleTL.crDirection.put("crossing_01_pedestrian", true);



            } else{
                // 红灯处理
                greenLight.setOffFlag(true);
                yellowLight.setOffFlag(true);
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
        if (yellowLight.getOffFlag() == false){
            rtnColor  = yellowLight.color;
        }
        if (redLight.getOffFlag() == false){
            rtnColor = redLight.color;
        }
        return rtnColor;
    }
}
