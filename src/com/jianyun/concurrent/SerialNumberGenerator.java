package com.jianyun.concurrent;

/**
 * Created by Administrator on 1/10/2016.
 */
public class SerialNumberGenerator extends IntGenerator{
    private int currentVale = 0;

    @Override
    public synchronized int next() {
        return currentVale++;
    }
}
