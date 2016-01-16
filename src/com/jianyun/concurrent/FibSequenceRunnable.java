package com.jianyun.concurrent;

import com.jianyun.generics.Fibonacci;

/**
 * Created by Administrator on 1/4/2016.
 */
public class FibSequenceRunnable implements Runnable{
    private int _count;
    public FibSequenceRunnable(int n){
        _count = n;
    }
    @Override
    public void run() {
        Fibonacci fibonacci = new Fibonacci();
        for(int i=0; i<_count; i++){
            System.out.println(fibonacci.next());
        }
    }
}
