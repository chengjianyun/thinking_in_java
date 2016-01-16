package com.jianyun.concurrent;

import jdk.internal.cmm.SystemResourcePressureImpl;
import jdk.internal.util.xml.impl.Input;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.*;

/**
 * Created by Administrator on 1/12/2016.
 */

class SleepBlocked implements Runnable{

    @Override
    public void run() {
        try {
            TimeUnit.SECONDS.sleep(100);
        } catch (InterruptedException e) {
            System.out.println("sleep is interrupted");
        }
        System.out.println("Exit SleepBlocked.run()");
    }
}

class IOBlocked implements Runnable{
    private InputStream in;
    public IOBlocked(InputStream is){
        in = is;
    }

    @Override
    public void run() {
        System.out.println("wait for read()");
        try {
            in.read();
        } catch (IOException e) {
            if(Thread.currentThread().isInterrupted()){
                System.out.println("Interrupt from blocked IO");
            } else {
                throw new RuntimeException();
            }
        }
        System.out.println("Exit IOBlocked.run()");
    }
}

class SyncronizedBlocked implements Runnable{

    private synchronized void f(){
        while(true){
            Thread.yield();
        }
    }

    public SyncronizedBlocked(){
        new Thread(){
            @Override
            public void run() {
                f();
            }
        }.start();
    }

    @Override
    public void run() {
        System.out.print("Trying to call f()");
        f();
        System.out.println("Exit from " + SyncronizedBlocked.class.getName());
    }
}

public class Interrupting {
    private static ExecutorService service = Executors.newCachedThreadPool();
    static void test(Runnable runnable) throws InterruptedException {
        Future<?> future = service.submit(runnable);
        TimeUnit.MILLISECONDS.sleep(100);
        System.out.println("Interrupting " + runnable.getClass().getName());
        future.cancel(true);
        System.out.println("Interrupt sent to " + runnable.getClass().getName());
    }

    public static void main(String... args) throws InterruptedException {
        test(new SleepBlocked());
        test(new IOBlocked(System.in));
        test(new SyncronizedBlocked());
        TimeUnit.SECONDS.sleep(3);
        System.out.println("Aborting with System.exit(0)");
        System.exit(0);
    }
}
