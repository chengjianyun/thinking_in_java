package com.jianyun.concurrent;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 1/10/2016.
 */
public class AtomicityIntegerTest implements Runnable{
    private AtomicInteger i = new AtomicInteger(0);

    public int getValue() {
        return i.get();
    }

    public void increment(){
        i.addAndGet(2);
    }

    @Override
    public void run() {
        while(true){
            increment();
        }
    }

    public static void main(String... args){
        AtomicityIntegerTest atit = new AtomicityIntegerTest();
        new Thread(atit){
            {setDaemon(true);}
        }.start();

        while(true){
            int value = atit.getValue();
            if(value%2 != 0){
                System.out.println(value);
                System.exit(0);
            }
        }
    }
}
