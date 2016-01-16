package com.jianyun.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class WaxOnOffTest {

    public static void main(String[] args) throws InterruptedException {
	// write your code here
        Car car = new Car();
        ExecutorService exec = Executors.newCachedThreadPool();
        exec.execute(new WaxOff(car));
        exec.execute(new WaxOn(car));
        TimeUnit.SECONDS.sleep(5);
        exec.shutdownNow();
    }
}

/**
 * Created by Administrator on 12/28/2015.
 */
class WaxOn implements Runnable {

    private Car _car;

    public WaxOn(Car _car) {
        this._car = _car;
    }

    @Override
    public void run() {
        try {
            while(!Thread.interrupted()){
                TimeUnit.MILLISECONDS.sleep(500);
                System.out.println("Wax On!");
                //                System.out.println(Thread.currentThread().getId()+"");
                _car.waxed();
                _car.waitForBuffing();
            }
        } catch (InterruptedException e) {
            System.out.println("Exiting for interrupt");
        }
        System.out.println("terminate of Wax off!");
    }
}

/**
 * Created by Administrator on 12/28/2015.
 */
class Car {
    private boolean waxOn = false;
    public synchronized void waxed() {
        waxOn = true;
        notifyAll();
    }

    public synchronized void buffed() {
        waxOn = false;
        notifyAll();
    }

    public synchronized void waitForWaxing() throws InterruptedException {
        while(!waxOn) {
            wait();
        }
    }

    public synchronized void waitForBuffing() throws InterruptedException {
        while(waxOn){
            wait();
        }
    }
}

/**
 * Created by Administrator on 12/28/2015.
 */
class WaxOff implements Runnable {
    private Car _car;

    public WaxOff(Car car) {
        _car = car;
    }

    @Override
    public void run() {
        try {
            while(!Thread.interrupted()) {
                System.out.println("Wax Off!");
                //                System.out.println(Thread.currentThread().getId() + "");
                TimeUnit.MILLISECONDS.sleep(500);
                _car.buffed();
                _car.waitForWaxing();
            }
        } catch (InterruptedException e) {
            System.out.println("Exiting for interrupt");
        }
        System.out.println("terminate of wax off!");
    }
}
