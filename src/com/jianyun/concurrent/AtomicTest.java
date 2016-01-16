package com.jianyun.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 1/10/2016.
 */
public class AtomicTest implements Runnable{
    private int i = 0;

    public int getValue(){
        return i;
    }

    private synchronized void increment(){
        i++;
        i++;
    }

    @Override
    public void run() {
        while (true){
            increment();
        }
    }

    public static void main(String... args){
        ExecutorService execs = Executors.newCachedThreadPool();
        AtomicTest at = new AtomicTest();
        execs.execute(at);
        while(true){
            int val = at.getValue();
            if(val%2 != 0){
                System.out.println(val);
                System.exit(0);
            }
        }
    }
}
