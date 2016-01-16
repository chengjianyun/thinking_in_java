package com.jianyun.concurrent;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Administrator on 1/10/2016.
 */
public class AttemptLocking {
    private ReentrantLock lock = new ReentrantLock();
    public void untimed(){
        boolean captured = lock.tryLock();
        try{
            System.out.println("tryLock() " + captured);
        } finally {
            if(captured){
                lock.unlock();
            }
        }
    }

    public void timed(){
        boolean captured = false;
        try {
            captured = lock.tryLock(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try{
            System.out.println("tryLock(2, TimeUnit.SECOND) " + captured);
        } finally {
            if(captured){
                lock.unlock();
            }
        }
    }

    public static void main(String... args) throws InterruptedException {
        final AttemptLocking attemptLocking = new AttemptLocking();
        attemptLocking.timed();
        attemptLocking.untimed();
        new Thread(){
            {setDaemon(true);}

            @Override
            public void run() {
//                attemptLocking.timed();
//                attemptLocking.untimed();
                boolean isCaptureed = attemptLocking.lock.tryLock();
                System.out.println("acquired " + isCaptureed);
                try {
                    Thread.sleep(3000000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                attemptLocking.lock.unlock();
            }
        }.start();
//        Thread.yield();
        Thread.sleep(100);
        attemptLocking.timed();
        attemptLocking.untimed();
    }
}
