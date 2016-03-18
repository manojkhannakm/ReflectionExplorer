package com.test;

/**
 * @author Manoj Khanna
 */

public class Test1 {

    private static Test1 test1 = new Test1();
    private int count;

    public static void main(String[] args) {
        new Thread(() -> {
            test1.count++;

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

}
