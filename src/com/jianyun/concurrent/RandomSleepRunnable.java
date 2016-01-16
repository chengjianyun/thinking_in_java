package com.jianyun.concurrent;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 1/4/2016.
 */
public class RandomSleepRunnable implements Runnable{

    @Override
    public void run() {
        Random r = new Random();
        int seconds = r.nextInt(10);
        try {
            TimeUnit.SECONDS.sleep(seconds);
            System.out.println(String.format("sleeped %d seconds", seconds));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
