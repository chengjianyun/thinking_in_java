package com.jianyun.concurrent;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by jocheng on 1/17/2016.
 */
public class Philosopher implements Runnable{
    private Chopstick right, left;
    private final int id;
    private final int ponderFactor;
    private Random rand = new Random(47);

    public Philosopher (int ident, int ponder, Chopstick first, Chopstick second){
        id = ident;
        ponderFactor = ponder;
        right = first;
        left = second;
    }

    public void pause(boolean isThinking) throws InterruptedException {
        if(isThinking) {
//            TimeUnit.MILLISECONDS.sleep(1);
            return;
        }
        TimeUnit.MILLISECONDS.sleep(50);
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                System.out.println(this + " thinking");
                pause(true);
                System.out.println(this + " grabbing right");
                right.taken();
                System.out.println(this + " grabbing left");
                left.taken();
                System.out.println(this + " eating");
                pause(false);
                right.drop();
                left.drop();
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
