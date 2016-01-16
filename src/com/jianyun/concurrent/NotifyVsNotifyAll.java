package com.jianyun.concurrent;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 1/2/2016.
 */
public class NotifyVsNotifyAll {
    public static void main(String... args) throws InterruptedException {
        ExecutorService execs = Executors.newCachedThreadPool();
        for(int i=0; i<5; i++) {
            execs.execute(new Task1());
        }
        execs.execute(new Task2());
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            boolean prod = true;
            @Override
            public void run() {
                if(prod) {
                    System.out.println("notify");
                    Task1.blocker.prod();
                    prod = false;
                } else {
                    System.out.println("notify All");
                    Task1.blocker.prodAll();
                    prod = true;
                }
            }
        }, 400, 400);

        TimeUnit.SECONDS.sleep(5);
        timer.cancel();
        System.out.println("timer canceled");

        TimeUnit.MILLISECONDS.sleep(500);
        System.out.println("Task2.blocker.prodAll()");
        Task2.blocker.prodAll();
        TimeUnit.MILLISECONDS.sleep(500);
        System.out.println("Shutting down");
        execs.shutdownNow();
    }
}

class Blocker {
    synchronized void waitForCall() throws InterruptedException {
        try{
            while(!Thread.interrupted()) {
                wait();
                System.out.println(Thread.currentThread() + "");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    synchronized void prod() {
        notify();
    }

    synchronized void prodAll() {
        notifyAll();
    }
}

class Task1 implements Runnable {

    public static Blocker blocker = new Blocker();
    @Override
    public void run() {
        try {
            blocker.waitForCall();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Task2 implements Runnable {

    public static Blocker blocker = new Blocker();
    @Override
    public void run() {
        try {
            blocker.waitForCall();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
