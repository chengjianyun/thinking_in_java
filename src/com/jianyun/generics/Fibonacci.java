package com.jianyun.generics;

/**
 * Created by Administrator on 1/3/2016.
 */
public class Fibonacci implements Generator<Integer> {
    private int count = 0;
    @Override
    public Integer next() {
        return fib(count++);
    }

    public int fib(int n){
        if(n<2){
            return 1;
        }
        return fib(n-2) + fib(n-1);
    }
}
