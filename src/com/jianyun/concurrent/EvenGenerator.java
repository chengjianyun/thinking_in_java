package com.jianyun.concurrent;

/**
 * Created by Administrator on 1/10/2016.
 */
public class EvenGenerator extends IntGenerator {
    private int currentEvenValue = 0;
    @Override
    public synchronized int next() {
        currentEvenValue ++;
        Thread.yield();
        currentEvenValue ++;
        return currentEvenValue;
    }

    public static void main(String... args){
        EvenChecker.test(new EvenGenerator());
    }
}
