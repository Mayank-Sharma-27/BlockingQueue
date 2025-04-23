package com.blockingqueue;

public class Main {

    public static void main(String args[]) throws Exception {
        final BlockingQueue blockingQueue = new BlockingQueue(5);
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < 50; i++) {
                        blockingQueue.enqueue(new Integer(i));
                        System.out.println("enqueued " + i);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < 25; i++) {
                        System.out.println("Thread 2 dequeued: " + blockingQueue.dequeue());
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        Thread t3 = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    for (int i = 0; i < 25; i++) {
                        System.out.println("Thread 3 dequeued: " + blockingQueue.dequeue());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        t1.start();
        Thread.sleep(400);
        t2.start();
        t3.start();
    }
}
