package com.jianyun.concurrent;

import org.omg.CORBA.SystemException;
import org.omg.CORBA.TIMEOUT;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 1/10/2016.
 */
public class SerialNumberChecker {
    private static final int SIZE = 10;
    private static CircularSet serials = new CircularSet(10);
    private static ExecutorService execs = Executors.newCachedThreadPool();
    private static SerialNumberGenerator generator = new SerialNumberGenerator();
    static class SerialChecker implements Runnable{

        @Override
        public void run() {
            while(true){
                int serial = generator.next();
                if(serials.contains(serial)){
                    System.out.println("duplicated " + serial);
                    System.exit(0);
                }
                serials.add(serial);
            }
        }
    }

    public static void main(String... args) throws InterruptedException {
        for (int i = 0; i < SIZE; i++) {
            execs.execute(new SerialChecker());
            if (args.length > 0) {
                TimeUnit.SECONDS.sleep(new Integer(args[0]));
                System.out.println("no duplicated checked");
                System.exit(0);
            }
        }
    }
}

class CircularSet{

    private int index = 0;
    private int[] numArray;
    private int len;

    public CircularSet(int size){
        numArray = new int[size];
        len = size;
        for(int i=0; i<size; i++){
            numArray[i]=-1;
        }
    }

    public synchronized void add(int i){
        numArray[index] = i;
        index = ++index%len;
    }

    public synchronized boolean contains(int value){
        for(int i=0; i<len; i++){
            if(numArray[i] == value)
                return true;
        }
        return false;
    }
}
