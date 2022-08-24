package com.ljp.concurr.chapter03;

public class NonStringThreadSafe {

    public static String allInfo = "";

    public static void main(String[] args){
        //追加含5个字符的apple
        Thread stringThread01 = new Thread(new AppendStringRunnable("apple"));
        //追加含3个字符的boy
        Thread stringThread02 = new Thread(new AppendStringRunnable("boy"));
        //追加含3个字符的cat
        Thread stringThread03 = new Thread(new AppendStringRunnable("cat"));
        //追加含4个字符的duck
        Thread stringThread04 = new Thread(new AppendStringRunnable("duck"));

        stringThread01.start();
        stringThread02.start();
        stringThread03.start();
        stringThread04.start();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        //输出到底有多少字符的信息量被追加到allInfo变量
        System.out.println(allInfo.length());

    }
}

class AppendStringRunnable implements Runnable {

    private String info = "";

    AppendStringRunnable(String info){
        this.info = info;
    }

    @Override
    public void run() {
        for (int i=0; i<1000; i++){
            NonStringThreadSafe.allInfo = NonStringThreadSafe.allInfo + info;
        }
    }
}
