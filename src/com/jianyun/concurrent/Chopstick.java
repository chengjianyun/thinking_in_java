package com.jianyun.concurrent;

/**
 * Created by jocheng on 1/17/2016.
 */
public class Chopstick {
    private boolean taken = false;
    public synchronized void taken() throws InterruptedException {
        while (taken) {
            wait();
        }
        taken = true;
    }
    public synchronized  void drop() {
        taken = false;
        notifyAll();
    }
}
