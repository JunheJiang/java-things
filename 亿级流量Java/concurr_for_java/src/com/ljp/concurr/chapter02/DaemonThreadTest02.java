package com.ljp.concurr.chapter02;

public class DaemonThreadTest02 {
    public static void main(String[] args){
        WithDaemonFriendThread withDaemonThread = new WithDaemonFriendThread();
        withDaemonThread.start();
    }
}
