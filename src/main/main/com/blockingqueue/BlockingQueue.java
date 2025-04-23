package com.blockingqueue;

import lombok.SneakyThrows;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BlockingQueue {

    int capacity;
    int size;
    int head;
    int tail;
    Integer[] arr;
    Lock lock;
    Condition condition;

    public BlockingQueue(int capacity) {
        this.capacity = capacity;
        arr = new Integer[capacity];
        lock = new ReentrantLock();
        condition = this.lock.newCondition();
    }

    @SneakyThrows
    public void enqueue(int item) {
        lock.lock();
        try {
            while (size == capacity) {
                condition.await();
            }

            if (tail == capacity) {
                tail = 0;
            }
            arr[tail] = item;
            size++;
            tail++;
            condition.signalAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            lock.unlock();
        }
    }

    @SneakyThrows
    public Integer dequeue() {
        Integer item = null;
        lock.lock();
        try {
            while (size == 0) {
                condition.await();
            }

            if (head == capacity) {
                head = 0;
            }
            item = arr[head];
            arr[head] = null;
            head++;
            size--;
            condition.signalAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            lock.unlock();
        }
        return item;
    }
}
