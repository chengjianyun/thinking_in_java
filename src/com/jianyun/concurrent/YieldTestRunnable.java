package com.jianyun.concurrent;

/**
 * Created by Administrator on 1/3/2016.
 */
public class YieldTestRunnable implements Runnable{
    private int id;
    public YieldTestRunnable(int id){
        this.id = id;
        System.out.println("construct YieldTestRunnable");
    }
    @Override
    public void run() {
        for(int i=0; i<3; i++){
            System.out.println("this is the id " + id + " the turn " + i);
            Thread.yield();
        }
        System.out.println(id + " run over.");
    }
}
