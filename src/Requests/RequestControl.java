package Requests;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class RequestControl {
    private ReentrantLock lock;
    private Condition cond;
    private int MAX;
    private int num;


    public RequestControl(int max) {
        lock = new ReentrantLock();
        cond = lock.newCondition();
        num = 0;
        MAX = max;
    }

    public ReentrantLock getLock() {
        return lock;
    }

    public void startRequest() throws InterruptedException {
        lock.lock();

        while(!(num < MAX)) cond.await();

        num++;

        lock.unlock();
    }

    public void endRequest() {
        lock.lock();

        if (!(num < MAX)) cond.signal();

        num--;

        lock.unlock();
    }

}
