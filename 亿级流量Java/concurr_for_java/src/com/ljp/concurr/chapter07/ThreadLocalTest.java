package com.ljp.concurr.chapter07;

import java.util.HashMap;
import java.util.Map;

public class ThreadLocalTest {

    public static void main(String[] args){
        ControllerBase controllerBase = new ControllerBase();

        // 创建两个线程，将同一个controllerBase参数放入
        Thread controllerThread01 =
                new Thread(new EmulationControllerRunnable(controllerBase));
        Thread controllerThread02 =
                new Thread(new EmulationControllerRunnable(controllerBase));

        controllerThread01.start();
        controllerThread02.start();

    }


}


class EmulationControllerRunnable implements Runnable{

    ControllerBase controllerBase;

    // 构造函数将外界的controllerBase设置为自身controllerBase
    EmulationControllerRunnable(ControllerBase controllerBase){
        this.controllerBase = controllerBase;
    }


    @Override
    public void run() {
        // 对controllerBase的内部ThreadLocal变量controllerName设值
        controllerBase.getControllerName().set(Thread.currentThread()
                .getName() + "-Controller");
        Map<String, String> mapParam = new HashMap<String, String>();

        mapParam.put("p01", Thread.currentThread().getName() + "_p01");
        mapParam.put("p02", Thread.currentThread().getName() + "_p02");

        // 对controllerBase的内部ThreadLocal变量controllerParams设值
        controllerBase.getControllerParams().set(mapParam);

        System.out.println("controller`s info: name--"
                + controllerBase.getControllerName().get() + ",and params: "
                + controllerBase.getControllerParams().get());

    }
}

// 控制器，内部带有多个ThreadLocal变量，
// 包含一个控制器命名变量和一个字符型key-value参数Map
class ControllerBase {

    ThreadLocal<String> controllerName = new ThreadLocal<String>();

    ThreadLocal<Map<String, String>> controllerParams =
            new ThreadLocal<Map<String, String>>();

    public ThreadLocal<String> getControllerName() {
        return controllerName;
    }

    public void setControllerName(ThreadLocal<String> controllerName) {
        this.controllerName = controllerName;
    }

    public ThreadLocal<Map<String, String>> getControllerParams() {
        return controllerParams;
    }

    public void setControllerParams(ThreadLocal<Map<String, String>>
                                            controllerParams) {
        this.controllerParams = controllerParams;
    }
}