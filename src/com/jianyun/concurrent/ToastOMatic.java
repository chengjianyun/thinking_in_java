package com.jianyun.concurrent;

import java.util.Random;
import java.util.concurrent.*;

/**
 * Created by jocheng on 1/16/2016.
 */
class Toast{
    public enum Status {
        DRY,
        BUTTERED,
        JAMMED
    }
    private Status status = Status.DRY;
    public final  int id;
    public Toast(int id){
        this.id = id;
    }
    public void butter(){
        this.status = Status.BUTTERED;
    }
    public void jam(Jam common, Jam peanut, Jam jelly){
        this.status = Status.JAMMED;
    }
    public Status getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Toast " + id + ": " + status;
    }
}

class ToastQueue extends LinkedBlockingQueue<Toast> {}

class Toaster implements Runnable {
    private ToastQueue toastQueue;
    private int count = 0;
    private Random rand = new Random(47);
    public Toaster(ToastQueue queue){
        toastQueue = queue;
    }
    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                TimeUnit.MILLISECONDS.sleep(100 + rand.nextInt(500));
                Toast toast = new Toast(count++);
                System.out.println(toast);
                toastQueue.put(toast);
            }
        } catch (InterruptedException e) {
            System.out.println("Toaster interrupted");
        }
        System.out.println("Toaster off");
    }
}

class Butterer implements Runnable {
    private ToastQueue dryQueue, butteredQueue;
    public Butterer(ToastQueue toastQueue, ToastQueue butteredQueue) {
        this.dryQueue = toastQueue;
        this.butteredQueue = butteredQueue;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Toast toast = dryQueue.take();
                toast.butter();
                System.out.println(toast);
                butteredQueue.put(toast);
            }
        } catch (InterruptedException e) {
            System.out.println("Butterer is interrupted");
        }
        System.out.println("Butterer off");
    }
}

class Jam {
    public enum Type {
        PEANUT,
        JELLY,
        COMMON
    }
    private Type type;
    public void setType(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }
}

class PeanutButterer implements Runnable{
    private LinkedBlockingQueue<Jam> peanutButterQueue;
    public PeanutButterer(LinkedBlockingQueue<Jam> queue) {
        peanutButterQueue = queue;
    }

    @Override
    public void run() {
        try{
            while (!Thread.currentThread().isInterrupted()){
                TimeUnit.MILLISECONDS.sleep(50);
                Jam jam = new Jam();
                jam.setType(Jam.Type.PEANUT);
                System.out.println("Jam " + jam.getType());
                peanutButterQueue.put(jam);
            }
        } catch (InterruptedException e) {
            System.out.println("PeanutButterer interrupted");
        }
        System.out.println("PeanutButterer off");
    }
}

class Jellyer implements Runnable {
    private LinkedBlockingQueue<Jam> jellyQueue;
    public Jellyer(LinkedBlockingQueue<Jam> queue) {
        jellyQueue = queue;
    }

    @Override
    public void run() {
        try{
            while (!Thread.currentThread().isInterrupted()){
                TimeUnit.MILLISECONDS.sleep(60);
                Jam jam = new Jam();
                jam.setType(Jam.Type.JELLY);
                System.out.println("Jam " + jam.getType());
                jellyQueue.put(jam);
            }
        } catch (InterruptedException e) {
            System.out.println("Jelly interrupted");
        }
        System.out.println("Jelly off");
    }
}

class Jammer implements Runnable {
    private ToastQueue _butteredQueue, _finishedQueue;
    private LinkedBlockingQueue<Jam> _peanutQueue, _jellyQueue;
    public Jammer(ToastQueue butteredQueue, ToastQueue finishedQueue, LinkedBlockingQueue<Jam> peanutQueue, LinkedBlockingQueue<Jam> jellyQueue){
        _butteredQueue = butteredQueue;
        _finishedQueue = finishedQueue;
        _peanutQueue = peanutQueue;
        _jellyQueue = jellyQueue;
    }
    private Jam makeJam() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(20);
        Jam common = new Jam();
        common.setType(Jam.Type.COMMON);
        return common;
    }

    @Override
    public void run() {
        try{
            while (!Thread.currentThread().isInterrupted()){
                Toast t = _butteredQueue.take();
                Jam peanut = _peanutQueue.take();
                Jam jelly = _jellyQueue.take();
                Jam common = makeJam();
                t.jam(common, peanut, jelly);
                System.out.println(t);
                _finishedQueue.put(t);
            }
        } catch (InterruptedException e) {
            System.out.println("Jammer is interrupted");
        }
        System.out.println("Jammer off");
    }
}

class Eater implements Runnable {
    private ToastQueue _finishedQueue;
    private int counter = 0;

    public Eater(ToastQueue finishedQueue){
        _finishedQueue = finishedQueue;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Toast t = _finishedQueue.take();
                if(t.id != counter++ || t.getStatus() != Toast.Status.JAMMED) {
                    System.out.println(">>>> Error " + t);
                    System.exit(1);
                } else {
                    System.out.println("Eating " + t);
                }
            }
        } catch (InterruptedException e) {
            System.out.println("Eater is interrupted");
        }
        System.out.println("Eater off");
    }
}

public class ToastOMatic {
    public static void main(String[] args) throws InterruptedException {
        ToastQueue toastQueue = new ToastQueue(),
                butteredQueue = new ToastQueue(),
                jammedQueue = new ToastQueue();
        LinkedBlockingQueue<Jam> peanutQueue = new LinkedBlockingQueue<>();
        LinkedBlockingQueue<Jam> jellyQueue = new LinkedBlockingQueue<>();
        ExecutorService exec = Executors.newCachedThreadPool();
        exec.execute(new Toaster(toastQueue));
        exec.execute(new Butterer(toastQueue, butteredQueue));
        exec.execute(new Jammer(butteredQueue, jammedQueue, peanutQueue, jellyQueue));
        exec.execute(new Eater(jammedQueue));
        exec.execute(new PeanutButterer(peanutQueue));
        exec.execute(new Jellyer(jellyQueue));
        TimeUnit.SECONDS.sleep(5);
        exec.shutdownNow();
    }
}
