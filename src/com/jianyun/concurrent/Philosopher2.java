package com.jianyun.concurrent;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by jocheng on 1/17/2016.
 */
public class Philosopher2 implements Runnable {
    private BlockingQueue<Chopstick> chopsticks;
    private final int id;
    private Random rand = new Random(47);

    public Philosopher2 (int ident, BlockingQueue<Chopstick> chopsticks){
        id = ident;
        this.chopsticks = chopsticks;
    }

    public void pause(boolean isThinking) throws InterruptedException {
        if(isThinking) {
//            TimeUnit.MILLISECONDS.sleep(1);
            return;
        }
        TimeUnit.MILLISECONDS.sleep(5);
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                System.out.println(this + " thinking");
                pause(true);
                System.out.println(this + " grabbing first");
                Chopstick first = chopsticks.take();
                Thread.yield();
                System.out.println(this + " grabbing second");
                Chopstick second = chopsticks.take();
                System.out.println(this + " eating");
                pause(false);
                chopsticks.put(first);
                chopsticks.put(second);
            }
        } catch (InterruptedException e) {
            System.out.println(this + " interrupted");
        }
    }

    @Override
    public String toString() {
        return "Philosopher " + id;
    }
}
