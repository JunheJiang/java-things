package com.ljp.concurr.chapter02;

public class DaemonThreadTest01 {
    public static void main(String[] args){
        DoOnceThread doOncePrintThread = new DoOnceThread();
        doOncePrintThread.start();
    }
}
