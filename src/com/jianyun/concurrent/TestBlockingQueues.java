package com.jianyun.concurrent;

import sun.rmi.runtime.RuntimeUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;

/**
 * Created by Administrator on 1/16/2016.
 */
class LiftOffRunner implements Runnable {
    private BlockingQueue<LiftOff> rockets;

    public LiftOffRunner(BlockingQueue<LiftOff> queue){
        rockets = queue;
    }

    public void add(LiftOff liftOff){
        try {
            rockets.put(liftOff);
        } catch (InterruptedException e) {
            System.out.println("Lift off runner interrupted during put()");
        }
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                LiftOff rocket = rockets.take();
                rocket.run();
            }
        } catch (InterruptedException e) {
            System.out.println("Waking from take()");
        }
        System.out.println("Exiting LiftOffRunner");
    }
}

public class TestBlockingQueues {
    static void getKey(){
        try {
            new BufferedReader(new InputStreamReader(System.in)).readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    static void getKey(String message){
        System.out.println(message);
        getKey();
    }
    static void test(String message, BlockingQueue<LiftOff> queue){
        System.out.println(message);
        LiftOffRunner runner = new LiftOffRunner(queue);
        Thread t = new Thread(runner);
        t.start();
//        for(int i=0;i<5;i++){
//            runner.add(new LiftOff(5));
//        }
        new Thread(){
            @Override
            public void run() {
                for(int i=0;i<5;i++){
                    runner.add(new LiftOff(5));
                }
            }
        }.start();

        getKey("Press 'Enter' (" + message + ")");
        t.interrupt();
        System.out.println("Finished " + message + " test");
    }

    public static void main(String[] args){
        test("LinkedBlockingQueue", new LinkedBlockingQueue<>());
        test("ArrayBlockingQueue", new ArrayBlockingQueue<>(3));
        test("SynchronousBlockingQueue", new SynchronousQueue<>());
    }
}
