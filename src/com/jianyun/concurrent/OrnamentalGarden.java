package com.jianyun.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 1/12/2016.
 */
class Count {
    private int count = 0;
    private Random rand = new Random(5);

    public synchronized int value(){
        return count;
    }

    public int increment(){
//        if(rand.nextBoolean()){
//            Thread.yield();
//        }
        int temp;
        synchronized (this) {
            count++;
            Thread.yield();
            temp = count;
        }
        return temp;
    }
}

class Entrance implements Runnable {
    private static Count counter = new Count();

    private int number = 0;
    private final int id;
    private static volatile boolean canceled = false;

    public Entrance(int id) {
        this.id = id;
    }

    public static void cancel(){
        canceled = true;
    }

    public synchronized int getValue(){
        return number;
    }

    public static int getTotalCount(){
        return counter.value();
    }

    @Override
    public String toString() {
        return "Entrance " + id + ": " + getValue();
    }

    @Override
    public void run() {
        while(!Thread.currentThread().isInterrupted()){
            ++number;
            System.out.println(this + " total: " + counter.increment());

            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                System.out.println("sleep is interrupted");
                break;
            }
        }
//        while(!canceled){
//            synchronized (this){
//                ++number;
//            }
//            System.out.println(this + " total: " + counter.increment());

//            try {
//                TimeUnit.MILLISECONDS.sleep(100);
//            } catch (InterruptedException e) {
//                System.out.println("sleep is interrupted");
//            }
//        }
        System.out.println("Stopping" + this);
    }
}



public class OrnamentalGarden {
    private static List<Entrance> entrances = new ArrayList<>();

    private static int sumEntrance(){
        int sum = 0;
        for(Entrance entrance:entrances){
            sum += entrance.getValue();
        }
        return sum;
    }

    public static void main(String... args) throws InterruptedException {
        ExecutorService exec = Executors.newCachedThreadPool();
        List<Future<?>> futures = new ArrayList<>();
        for(int i = 0; i<5; i++){
            Entrance item = new Entrance(i);
            entrances.add(item);
            futures.add(exec.submit(item));
        }

        TimeUnit.SECONDS.sleep(3);
//        Entrance.cancel();
        exec.shutdown();

        for(Future<?> task : futures){
            task.cancel(true);
        }
        if(!exec.awaitTermination(250,TimeUnit.MICROSECONDS)){
            System.out.println("Some of tasks are not terminated");
        }
        System.out.println("Total: " + Entrance.getTotalCount());
        System.out.println("Sum of the entrances: " + sumEntrance());
    }
}
