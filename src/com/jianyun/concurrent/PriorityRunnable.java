package com.jianyun.concurrent;

/**
 * Created by Administrator on 1/4/2016.
 */
public class PriorityRunnable implements Runnable {
    private int _countDown = 5;
    private volatile double _d;
    private int priority;

    public PriorityRunnable(int priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return Thread.currentThread() + ": " + _countDown;
    }

    @Override
    public void run() {
        Thread.currentThread().setPriority(priority);
        while(true){
            for(int i=0;i<100000;i++){
                _d+=(Math.PI + Math.E)/(double)i;
                if(i % 100 == 0){
                    Thread.yield();
                }
            }
            System.out.println(this);
            if(--_countDown == 0){
                return;
            }
        }
    }
}
