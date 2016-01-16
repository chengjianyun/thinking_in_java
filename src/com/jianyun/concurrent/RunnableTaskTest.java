package com.jianyun.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 1/3/2016.
 */
public class RunnableTaskTest {
    public static void main(String... args) throws InterruptedException {
//        signalThreadRun();
//        directRun();
        moreThreadRun();
//        executorRun();
    }

    private static void directRun(){
        LiftOff launch = new LiftOff();
        launch.run();
    }

    private static void signalThreadRun(){
        Thread thread = new Thread(new LiftOff());
        thread.start();
        System.out.println("wait for lift off.");
    }

    private static void moreThreadRun() throws InterruptedException {
        for(int i = 0; i<6; i++){
//            Thread thread = new Thread(new LiftOff());
//            Thread thread = new Thread(new YieldTestRunnable(i));
//            Thread thread = new Thread(new FibSequenceRunnable(i));
//            Thread thread = new Thread(new FibSequenceRunnable(i));
//            Thread thread = new Thread(new RandomSleepRunnable());
//            Thread thread = new Thread(new PriorityRunnable(Thread.MIN_PRIORITY));
            Thread thread = new Thread(new SimpleDaemonRunnable());
            thread.setDaemon(true);
            thread.start();
        }
        TimeUnit.MILLISECONDS.sleep(2005);
    }

    private static void executorRun(){
        ExecutorService execs = Executors.newCachedThreadPool();
        for(int i = 0; i<5; i++){
//            execs.execute(new LiftOff());
//            execs.execute(new YieldTestRunnable(i));
            execs.execute(new PriorityRunnable(Thread.MIN_PRIORITY));
        }
        execs.execute(new PriorityRunnable(Thread.MAX_PRIORITY));
        execs.shutdownNow();
    }
}