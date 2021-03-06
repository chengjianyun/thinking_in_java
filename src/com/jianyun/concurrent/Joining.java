package com.jianyun.concurrent;

import jdk.nashorn.internal.runtime.regexp.JoniRegExp;

/**
 * Created by Administrator on 1/5/2016.
 */
public class Joining {
    public static void main(String... args) {
        Sleeper
                sleepy = new Sleeper("sleepy", 1500),
                grumpy = new Sleeper("grumpy", 2000);

        Joiner
                dopey = new Joiner("dopey", sleepy),
                doc = new Joiner("doc", grumpy);
//        dopey.start();
//        doc.start();
        grumpy.interrupt();
    }
}

class Sleeper extends Thread {
    private int duration;
    public Sleeper(String name, int sleepTime){
        super(name);
        duration = sleepTime;
        start();
    }

    @Override
    public void run() {
        try{
            sleep(duration);
        } catch (InterruptedException e) {
            System.out.println(getName() + " " + e.getMessage());
        }
        System.out.println(getName() + " has awakened");
    }
}

class Joiner extends Thread {
    private Sleeper sleeper;

    public Joiner(String name, Sleeper sleeper) {
        super(name);
        this.sleeper = sleeper;
        start();
    }

    @Override
    public void run() {
        try{
            sleeper.join();
        } catch (InterruptedException e) {
            System.out.println(getName() + " " + e.getMessage());
        }
        System.out.println(getName() + " has completed");
    }
}
