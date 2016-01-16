package com.jianyun.concurrent;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Administrator on 1/12/2016.
 */
class BlockedMutex {
    ReentrantLock lock = new ReentrantLock();

    public BlockedMutex(){
        lock.lock();
    }

    public void f(){
        try {
            lock.lockInterruptibly();
            System.out.println("lock acquired from f()");
        } catch (InterruptedException e) {
            System.out.println("Interrupt from BlockMutex.f()");
        }
    }
}

class Blocked2 implements Runnable {
    BlockedMutex mutex = new BlockedMutex();
    @Override
    public void run() {
        System.out.println("wait for f() in BlockedMutex");
        mutex.f();
        System.out.println("Broken out of the call");
    }
}

public class Interrupting2 {
    public static void main(String... args) throws InterruptedException {
        Thread t = new Thread(new Blocked2());
        t.start();
        TimeUnit.SECONDS.sleep(1);
        System.out.println("Issuing t.interrupt()");
        t.interrupt();
    }
}
