package com.jianyun.concurrent;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Administrator on 1/4/2016.
 */
public class CallableTaskTest {
    public static void main(String... args){
        fibSumResult();
    }


    private static void taskWithTResult(){
        ExecutorService execs = Executors.newCachedThreadPool();
        ArrayList<Future<String>> results = new ArrayList<Future<String>>();
        for(int i=0; i<10; i++){
            results.add(execs.submit(new TaskWithResultsCallable(i)));
        }
        for(Future<String> item : results){
            try {
                System.out.println(item.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } finally {
                execs.shutdown();
            }
        }
    }

    private static void fibSumResult(){
        ExecutorService execs = Executors.newCachedThreadPool();
        ArrayList<Future<Integer>> results = new ArrayList<Future<Integer>>();
        for(int i=0; i<10; i++){
            results.add(execs.submit(new FibSumCallable(i)));
        }
        for(Future<Integer> item : results){
            try {
                System.out.println(item.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } finally {
                execs.shutdown();
            }
        }
    }
}
