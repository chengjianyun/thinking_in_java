package com.jianyun.concurrent;

import java.io.IOException;
import java.util.concurrent.*;

/**
 * Created by jocheng on 1/17/2016.
 */
public class DiningPhilosopherWhitBin {
    public static void main(String... args) throws InterruptedException, IOException {
        int ponder = 5;
        if (args.length > 0) {
            ponder = Integer.parseInt(args[0]);
        }
        int size = 5;
        if (args.length > 0) {
            size = Integer.parseInt(args[1]);
        }
        ExecutorService exec = Executors.newCachedThreadPool();
        BlockingQueue<Chopstick> chopsticks = new ArrayBlockingQueue<Chopstick>(size);
        for (int i = 0; i < size; i++) {
            chopsticks.put(new Chopstick());
        }
        for (int i = 0; i < size; i++) {
            exec.execute(new Philosopher2(i, chopsticks));
        }

        if(args.length > 3 && args[2].equals("timeout")) {
            TimeUnit.SECONDS.sleep(5);
        } else {
            System.out.println("Press 'Enter' to quit");
            System.in.read();
        }
        exec.shutdownNow();
    }
}

