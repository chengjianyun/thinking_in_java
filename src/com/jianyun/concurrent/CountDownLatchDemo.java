package com.jianyun.concurrent;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by jocheng on 1/17/2016.
 */
class TaskPortion implements Runnable {
    private static int count = 0;
    private final int id = count++;
    private static Random rand = new Random(47);
    private final CountDownLatch latch;

    public TaskPortion(CountDownLatch latch) {
        this.latch = latch;
    }


    @Override
    public void run() {
        try {
            doSomeWork();
            latch.countDown();
        } catch (InterruptedException e) {
            System.out.println(this + " exit for interruption");
        }
    }

    private void doSomeWork() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(rand.nextInt(2000));
        System.out.println(this + " complete");
    }

    @Override
    public String toString() {
        return String.format("%1$-3d ", id);
    }
}

class WaitTask implements Runnable {
    private CountDownLatch latch;
    private static int count = 0;
    private final int id = count++;
    public WaitTask(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void run() {
        try{
            latch.await();
            System.out.println("latch barrier passed for " + this);
        } catch (InterruptedException e) {
            System.out.println("WaitTask interrupted exit");
        }
    }

    @Override
    public String toString() {
        return String.format("waitingTask %1$-3d ", id);
    }
}

public class CountDownLatchDemo {
    static final int SIZE = 100;
    public static void main(String[] args){
        ExecutorService exec = Executors.newCachedThreadPool();
        CountDownLatch latch = new CountDownLatch(SIZE);
        for (int i = 0; i < 10; i++) {
            exec.execute(new WaitTask(latch));
        }
        for (int i = 0; i < SIZE; i++) {
            exec.execute(new TaskPortion(latch));
        }
        System.out.println("launch all tasks");
        exec.shutdown();
    }
}
