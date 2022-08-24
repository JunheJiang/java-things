package com.yzxie.java.demo.charter3.part_3_3;

/**
 * @author xieyizun
 * @date 18/6/2019 15:43
 * @description:
 */
public class VolatileDemo2 {
    private int sum = 1;
    public boolean isUpdate = false;

    // 使用volatile修饰避免发生指令重排
    //public volatile boolean isUpdate = false;

    // 内部的sum和isUpdate没有依赖关系，故可能会进行指令重排
    public void updateSum() {
        // 修改sum的值为2
        sum = 2;
        isUpdate = true;
    }

    public void doWork() {
        updateSum();
        // 如果updateSum方法发送了指令重排，即isUpdate = true先执行，sum=2后执行，
        // 在这之间其他线程执行了sum+=1后，sum等于2
        if (isUpdate) {
            sum += 1;
        }
        // 预期结果为3，不过由于指令重排，则可能为2
        System.out.println("sum = " + sum);
    }

    public static void main(String[] args) {
        VolatileDemo2 demo = new VolatileDemo2();
        // 子线程
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                // 等待isUpdate变为true
                while (!demo.isUpdate) {}

                demo.doWork();
            }
        });
        thread.start();
        // 主线程
        demo.updateSum();
    }
}
