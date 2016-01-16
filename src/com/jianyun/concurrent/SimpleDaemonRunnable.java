package com.jianyun.concurrent;

import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 1/4/2016.
 */
public class SimpleDaemonRunnable implements Runnable{
    @Override
    public void run() {
        while (true){
            try {
                TimeUnit.MILLISECONDS.sleep(1000);
                System.out.println(Thread.currentThread() + "" + this);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
