package com.jianyun.concurrent;

/**
 * Created by Administrator on 1/12/2016.
 */
public class MultiLock {
    public synchronized void f1(int count){
        if(count-- > 0){
            System.out.println("f1() is calling f2() with count: " + count);
            f2(count);
        }
    }

    private synchronized void f2(int count) {
        if(count-- > 0){
            System.out.println("f2() is calling f1() with count: " + count);
            f1(count);
        }
    }

    public static void main(String... args){
        final MultiLock ml = new MultiLock();
        new Thread() {
            @Override
            public void run() {
                ml.f1(10);
            }
        }.start();
    }
}
