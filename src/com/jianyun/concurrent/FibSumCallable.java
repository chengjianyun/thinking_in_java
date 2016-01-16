package com.jianyun.concurrent;

import com.jianyun.generics.Fibonacci;

import java.util.concurrent.*;

/**
 * Created by Administrator on 1/4/2016.
 */
public class FibSumCallable implements Callable<Integer>{
    private int _count;
    private Fibonacci _fib;
    public FibSumCallable(int n){
        _fib = new Fibonacci();
        _count = n;
    }

    @Override
    public Integer call() throws Exception {
        int sum = 0;
        for(int i=0; i<_count; i++){
            sum += _fib.next();
        }
        return sum;
    }

    public Future<Integer> runTask() {
        FutureTask<Integer> task = new FutureTask<Integer>(this);
        Thread thread = new Thread(task);
        thread.start();
        return task;
//        ExecutorService executorService = Executors.newCachedThreadPool();
//        Future<Integer> result = executorService.submit(this);
//        executorService.shutdownNow();
//        return result;
    }

    public static void main(String... args) throws ExecutionException, InterruptedException {
        Future<Integer> result = new FibSumCallable(6).runTask();
        System.out.println(result.get().toString());
    }
}
