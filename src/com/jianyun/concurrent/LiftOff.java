package com.jianyun.concurrent;

/**
 * Created by Administrator on 1/3/2016.
 */
public class LiftOff implements Runnable{
    private int _countDown = 10;
    private static int taskCount = 0;
    private int id = taskCount++;

    public LiftOff(){}
    public LiftOff(int countDown){
        _countDown = countDown;
    }

    public String status(){
        if(_countDown > 0) {
            return String.format("#%d(%d),", id, _countDown);
        } else {
            return String.format("#%d(%s),", id, "liftoff");
        }
    }
    @Override
    public void run() {
        while(_countDown-- > 0){
            System.out.println(status());
            Thread.yield();
        }
    }
}
