package com.jianyun.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 1/10/2016.
 */
public class EvenChecker implements Runnable {
    private IntGenerator generator;
    private final int id;
    public EvenChecker(IntGenerator generator, int ident) {
        this.generator = generator;
        id = ident;
    }

    @Override
    public void run() {
        while(!generator.isCanceled()){
            int val = generator.next();
            if(val%2 != 0){
                System.out.println(val + " not even");
                generator.cancel();
            }
        }
    }

    public static void test(IntGenerator generator, int count){
        System.out.println("Press Control-C to exit");
        ExecutorService execs = Executors.newCachedThreadPool();
        for(int i=0; i<count; i++){
            execs.execute(new EvenChecker(generator, i));
        }
        execs.shutdown();
    }

    public static void test(IntGenerator gp) {
        test(gp, 10);
    }
}
