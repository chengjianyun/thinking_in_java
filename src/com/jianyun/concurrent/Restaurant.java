package com.jianyun.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 1/14/2016.
 */
class Meal{
    private int orderNum;
    public Meal(int num){
        orderNum = num;
    }

    @Override
    public String toString() {
        return String.format("Meal #%d", orderNum);
    }
}

class WaitPerson implements Runnable{
    private Restaurant restaurant;
    public WaitPerson(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    private void waitForMeal() throws InterruptedException {
        synchronized (this){
            while (restaurant.meal == null) {
                System.out.println("waiter wait for meal");
                wait();
            }
        }
    }

    private void waitForCleanUp() throws InterruptedException {
        synchronized (this) {
            while (!restaurant.isCleaned) {
                System.out.println("waiter wait for clean up!");
                wait();
            }
        }
    }

    private void notifyCleanedUp() {
        synchronized (restaurant.busBoy){
            System.out.println("plate is dirty, notify clean up");
            restaurant.isCleaned = false;
            restaurant.busBoy.notifyAll();
        }
    }

    private void notifyChef() throws InterruptedException {
        synchronized (restaurant.chef){
            restaurant.meal = null;
            System.out.println("meal is consumed, need another");
            notifyCleanedUp();
            waitForCleanUp();
            restaurant.chef.notifyAll();
            System.out.println("waiter notify chef to do another meal");
        }
    }
    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                waitForMeal();
                System.out.println("got the " + restaurant.meal);
                notifyChef();
//                synchronized (restaurant) {
//                    if (restaurant.meal == null) {
//                        System.out.println("waiting for meal...");
//                        restaurant.wait();
//                    } else {
//                        System.out.println("got the " + restaurant.meal);
//                        restaurant.meal = null;
//                        restaurant.notify();
//                    }
//                }
            }
        } catch (InterruptedException e){
            System.out.println("Interrupted from WaitPerson " + e.getMessage());
        }
    }
}

class BusBoy implements Runnable {
    private Restaurant restaurant;
    public BusBoy(Restaurant restaurant){
        this.restaurant = restaurant;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                synchronized (this) {
                    while (restaurant.isCleaned) {
                        System.out.println("busboy wait for notify");
                        wait();
                    }
                }
                System.out.println("BusBoy cleaning up...");
                TimeUnit.MILLISECONDS.sleep(300);
                synchronized (restaurant.waiter) {
                    restaurant.isCleaned = true;
                    restaurant.waiter.notifyAll();
                    System.out.println("cleaned up, notify");
                }
            }
        } catch (InterruptedException e) {
            System.out.println("Interrupted from BusBoy" + e.getMessage());
        }

    }
}
class Chef implements Runnable {
    private int count = 0;
    private Restaurant restaurant;

    public Chef(Restaurant restaurant){
        this.restaurant = restaurant;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                synchronized (this){
                    while (restaurant.meal != null){
                        System.out.println("chef wait for an empty plate");
                        wait();
                    }
                }
                if(++count > 10){
                    System.out.println("Out of food, Closing.");
                    restaurant.exec.shutdownNow();
                }

                System.out.println("order up! ");
                synchronized (restaurant.waiter){
                    restaurant.meal = new Meal(count);
                    System.out.println("make a meal, notify waiter");
                    restaurant.waiter.notifyAll();
                }
                TimeUnit.MILLISECONDS.sleep(200);
//                synchronized (restaurant) {
//                    if (restaurant.meal != null) {
//                        restaurant.wait();
//                    } else {
//                        TimeUnit.MILLISECONDS.sleep(200);
//                        restaurant.meal = new Meal(count++);
//                        restaurant.notify();
//                        System.out.println("make meal done, notify waiter to get it");
//                    }
//                }
            }
        } catch (InterruptedException e){
                System.out.println("Interrupted from Chef " + e.getMessage());
        } finally {
            System.out.println("clef cleaning...");
        }
    }
}

public class Restaurant {
    public Meal meal;
    public boolean isCleaned = true;
    public BusBoy busBoy = new BusBoy(this);
    public WaitPerson waiter = new WaitPerson(this);
    public Chef chef = new Chef(this);
    public ExecutorService exec = Executors.newCachedThreadPool();

    public static void main(String... args) throws InterruptedException {
        Restaurant restaurant = new Restaurant();
        restaurant.exec.execute(restaurant.waiter);
        restaurant.exec.execute(restaurant.busBoy);
        TimeUnit.MILLISECONDS.sleep(100);
        restaurant.exec.execute(restaurant.chef);
    }
}
