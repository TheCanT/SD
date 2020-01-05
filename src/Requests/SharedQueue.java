package Requests;

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class SharedQueue<E> {
    private Queue<E> queue;
    private ReentrantLock queue_lock;
    private Condition queue_is_empty;
    private Condition queue_is_full;
    private int num_isFull_waiting;


    public SharedQueue() {
        this.queue = new PriorityQueue<E>();
        this.queue_lock = new ReentrantLock();
        this.queue_is_empty = queue_lock.newCondition();
        this.queue_is_full = queue_lock.newCondition();
        this.num_isFull_waiting = 0;
    }


    public boolean offer(E n) {
        this.queue_lock.lock();

        int size_queue = queue.size();

        boolean bool;
        do {
            bool = queue.offer(n);
            this.queueIsFull(!bool);
        } while (!bool);

        if (size_queue == 0) queue_is_empty.signal();

        this.queue_lock.unlock();
        return bool;
    }


    public E poll() {
        this.queue_lock.lock();

        this.queueIsEmpty();

        E n = queue.poll();

        if (num_isFull_waiting > 0) queue_is_full.signal();

        this.queue_lock.unlock();
        return n;
    }


    private void queueIsEmpty() {
        while (queue.size() < 1) {
            try {
                queue_is_empty.await();
            } catch (InterruptedException ignore) {
            }
        }
    }


    private void queueIsFull(boolean bool) {
        this.num_isFull_waiting++;
        while (bool) {
            try {
                queue_is_full.await();
                bool = false;
            } catch (InterruptedException ignore) {
            }
        }
        this.num_isFull_waiting--;
    }


    public boolean add(E n) {
        this.queue_lock.lock();

        int size_queue = queue.size();

        boolean bool;
        do {
            bool = queue.add(n);
            this.queueIsFull(!bool);
        } while (!bool);

        if (size_queue == 0) queue_is_empty.signal();

        this.queue_lock.unlock();
        return bool;
    }


    public E remove() {
        this.queue_lock.lock();

        this.queueIsEmpty();

        E n = queue.remove();

        if (num_isFull_waiting > 0) queue_is_full.signal();

        this.queue_lock.unlock();
        return n;
    }


    public E element() {
        this.queue_lock.lock();

        this.queueIsEmpty();

        E n = queue.element();

        if (num_isFull_waiting > 0) queue_is_full.signal();

        this.queue_lock.unlock();
        return n;
    }


    public E peek() {
        this.queue_lock.lock();

        this.queueIsEmpty();

        E n = queue.peek();

        if (num_isFull_waiting > 0) queue_is_full.signal();

        this.queue_lock.unlock();
        return n;
    }
}
