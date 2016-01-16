package com.jianyun.concurrent;

import java.util.concurrent.Callable;

/**
 * Created by Administrator on 1/3/2016.
 */
public class TaskWithResultsCallable implements Callable<String>{
    private int id;
    public TaskWithResultsCallable(int id){
        this.id = id;
    }
    @Override
    public String call() throws Exception {
        return "result of TaskWithResult " + id;
    }
}
