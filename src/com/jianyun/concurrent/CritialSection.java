package com.jianyun.concurrent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 1/10/2016.
 */
public class CritialSection {

    static void testApproaches(PairManager pmang1, PairManager pmag2) throws InterruptedException {
        ExecutorService exec = Executors.newCachedThreadPool();
        PairManiplutor
                pm1 = new PairManiplutor(pmang1),
                pm2 = new PairManiplutor(pmag2);

        PairChecker
                pc1 = new PairChecker(pmang1),
                pc2 = new PairChecker(pmag2);

        exec.execute(pm1);
        exec.execute(pm2);
        exec.execute(pc1);
        exec.execute(pc2);

        TimeUnit.MILLISECONDS.sleep(500);

        System.out.println("pm1: " + pm1 + "\npm2: " + pm2);
        System.exit(0);
    }

    public static void main(String... args){
        PairManager
                pmag1 = new PairManager1(),
                pmag2 = new PairManager2();

        try {
            testApproaches(pmag1, pmag2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Pair{
    private int x, y;

    public Pair(){
        this(0,0);
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public Pair(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void incrementX(){
        x++;
    }

    public void incrementY(){
        y++;
    }

    public void checkStatus(){
        if(x!=y){
            throw new PairNotEqualException();
        }
    }

    @Override
    public String toString() {
        return "x: " + x + ", y: " + y;
    }

    class PairNotEqualException extends RuntimeException {
        public PairNotEqualException(){
            super("pair value not equal: " + Pair.this);
        }
    }
}

abstract class PairManager {
    AtomicInteger checkCounter = new AtomicInteger(0);
    protected Pair p = new Pair();
    private List<Pair> stores = Collections.synchronizedList(new ArrayList<Pair>());

    public synchronized Pair getPair(){
        return new Pair(p.getX(), p.getY());
    }

    public void store(Pair p) {
        stores.add(p);
        try {
            TimeUnit.MILLISECONDS.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public abstract void increment();
}

class PairManager1 extends PairManager{

    @Override
    public synchronized void increment() {
        p.incrementX();
        p.incrementY();
        store(getPair());
    }
}

class PairManager2 extends PairManager{

    @Override
    public void increment() {
        Pair temp;
        synchronized (this){
            p.incrementX();
            p.incrementY();
            temp = getPair();
        }
        store(temp);
    }
}

class PairManiplutor implements Runnable{
    private PairManager manager;

    public PairManiplutor(PairManager manager){
        this.manager = manager;
    }
    @Override
    public void run() {
        while(true){
            manager.increment();
        }
    }

    @Override
    public String toString() {
        return "Pair: " + manager.getPair() + "; CheckCounter = " + manager.checkCounter.get();
    }
}

class PairChecker implements Runnable{
    private PairManager pm;
    public PairChecker(PairManager pm){
        this.pm = pm;
    }

    @Override
    public void run() {
        while(true){
            pm.checkCounter.incrementAndGet();
            pm.getPair().checkStatus();
        }
    }
}

