package com.jianyun.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 1/2/2016.
 */
public class BusyWaitTest {
    public static void main(String... args) throws InterruptedException {
        BusyWaitRunnable br = new BusyWaitRunnable();
        setSignalRunnable sr = new setSignalRunnable(br);
        ExecutorService execs = Executors.newCachedThreadPool();
        execs.execute(br);
        execs.execute(sr);
        TimeUnit.SECONDS.sleep(5);
        execs.shutdownNow();
    }
}

class BusyWaitRunnable implements Runnable{
    private boolean canRun = false;
    public synchronized void setAvailable() {
        canRun = true;
        notifyAll();
    }

    public synchronized void checkAvailable() throws InterruptedException {
        while(!canRun){
            wait();
        }
    }

    @Override
    public void run() {
        long starttime = System.nanoTime();
        try {
            checkAvailable();
            System.out.println("reset can run to false.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long stoptime = System.nanoTime();
        System.out.println("busy wait run time: " + (stoptime - starttime));
    }
}

class setSignalRunnable implements Runnable{

    private BusyWaitRunnable waitRunnable;
    public setSignalRunnable(BusyWaitRunnable runnable) {
        waitRunnable = runnable;
    }

    @Override
    public void run() {
        try {
            TimeUnit.SECONDS.sleep(3);
            waitRunnable.setAvailable();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

