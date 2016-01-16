package com.jianyun.concurrent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 1/2/2016.
 */

public class WaitNotifyTest{

    public static void main(String... args) throws InterruptedException {
        MessageRunnable r1 = new MessageRunnable("this is 1.");
        MessageRunnable r2 = new MessageRunnable("this is 2.");
        TimerRunnable tr = new TimerRunnable(r1, r2);

        ExecutorService execs = Executors.newCachedThreadPool();
        execs.execute(r1);
        execs.execute(r2);
        execs.execute(tr);

        TimeUnit.SECONDS.sleep(10);
        execs.shutdownNow();
    }
}

class MessageRunnable implements  Runnable{
    private String message;
    public MessageRunnable(String msg){
        message = msg;
    }
    public synchronized  static void notifyToRun(){
//        (MessageRunnable.class){
            MessageRunnable.class.notifyAll();
//        }
    }
    public void startWait() throws InterruptedException {
        synchronized (MessageRunnable.class) {
            System.out.println(message + " start to wait");
            MessageRunnable.class.wait();
            printMessage();
        }
    }
    private void printMessage(){
        System.out.println(message + " is notified");
    }

    @Override
    public void run(){
        try {
            startWait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class TimerRunnable implements Runnable{

    private List<MessageRunnable> msgRunable;
    public TimerRunnable(MessageRunnable... runnables){
        msgRunable = new ArrayList<>();
        for(MessageRunnable item : runnables) {
            msgRunable.add(item);
        }
    }

    @Override
    public void run(){
        Iterator<MessageRunnable> iterator = msgRunable.iterator();
        while(iterator.hasNext()) {
            MessageRunnable runnable = iterator.next();
            try {
                TimeUnit.SECONDS.sleep(3);
                // the below code will notify all message runnable
//                synchronized (MessageRunnable.class) {
//                    MessageRunnable.class.notifyAll();
//                }
                // very weird for this function only notify the runnable itself
                MessageRunnable.notifyToRun();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
