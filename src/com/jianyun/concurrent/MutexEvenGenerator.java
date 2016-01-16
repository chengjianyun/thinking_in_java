package com.jianyun.concurrent;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Administrator on 1/10/2016.
 */
public class MutexEvenGenerator extends IntGenerator{
    private int currentEvenVale = 0;
    private Lock lock = new ReentrantLock();
    @Override
    public int next() {
        lock.lock();
        try {
            ++currentEvenVale;
            Thread.yield();
            ++currentEvenVale;
            return currentEvenVale;
        } finally {
            lock.unlock();
        }
    }

    public static void main(String... args){
        EvenChecker.test(new MutexEvenGenerator());
    }
}
