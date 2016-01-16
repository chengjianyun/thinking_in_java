package com.jianyun.concurrent;

import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 1/9/2016.
 */
public class SubThreadException {
    static Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            throw new RuntimeException();
        }
    });

    public static void main(String... args) throws InterruptedException {
        try {
//            thread.setUncaughtExceptionHandler(new MyUnCatchableExceptionHandler());
            thread.start();
            System.out.println("can run to here");
        } catch(Exception e) {
            System.out.print("sub thread message: " + e.getMessage());
        }
        TimeUnit.SECONDS.sleep(5);
        System.out.println("main class exit safely");
    }
}

class MyUnCatchableExceptionHandler implements Thread.UncaughtExceptionHandler {

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        System.out.println("sub thread message from my unCatchableException: " + e.getMessage());
    }
}
