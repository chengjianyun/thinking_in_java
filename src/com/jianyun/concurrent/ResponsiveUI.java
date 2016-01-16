package com.jianyun.concurrent;

import java.io.IOException;

/**
 * Created by Administrator on 1/5/2016.
 */
public class ResponsiveUI extends Thread {
    private static double d = 1;
    public ResponsiveUI() {
        setDaemon(true);
        start();
    }

    @Override
    public void run() {
        while (d > 0) {
            d += Math.PI + Math.E;
        }
    }

    public static void main(String... args) throws IOException {
//        ResponsiveUI responsive = new ResponsiveUI();
        UnresponsiveUI unresponsiveUI = new UnresponsiveUI();
        System.in.read();
        System.out.println( d + "");
    }
}

class UnresponsiveUI {
    private double d = 1;

    public UnresponsiveUI() throws IOException {
        while (d > 0) {
            d += Math.PI + Math.E;
        }
        System.in.read();
    }
}
