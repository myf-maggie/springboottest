package com.test;

/**
 * Created by zxc on 2020/4/5.
 */
public class Deilock extends Thread{
    private static Object object1=new Object();
    private static Object object2=new Object();

    public static void main(String[] args) {
        Deilock deilock=new Deilock();deilock.start();
        Thread thread=new Thread(new Thred1());thread.start();
    }
    @Override
    public void run() {
        super.run();
        synchronized (object2){
            System.out.println("B get lock :object2");
            try {
                Thread.sleep(500);
                synchronized (object1){
                    System.out.println("B get lock :object1");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    static class Thred1 extends Thread{
        @Override
        public void run() {
            synchronized (object1){
                System.out.println("A get lock :object1");
                synchronized (object2){
                    System.out.println("A get lock :object2");
                }
            }
        }
    }


}