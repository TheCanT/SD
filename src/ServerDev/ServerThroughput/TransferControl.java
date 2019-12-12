package ServerDev.ServerThroughput;

import java.net.Socket;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class TransferControl {


    private ReentrantLock lock_down;
    private Condition cond_down;
    private static final int MAX_DOWN = 1;
    private int num_down;


    private ReentrantLock lock_up;
    private Condition cond_up;
    private static final int MAX_UP = 1;
    private int num_up;



    public TransferControl(){
        lock_down = new ReentrantLock();
        cond_down = lock_down.newCondition();
        num_down  = 0;

        lock_up = new ReentrantLock();
        cond_up = lock_up.newCondition();
        num_up  = 0;
    }



    public ReentrantLock getLockDown() {
        return lock_down;
    }

    public ReentrantLock getLockUp() {
        return lock_up;
    }
    public void startDownload() throws InterruptedException {
        lock_down.lock();

        while(!(num_down<MAX_DOWN)) cond_down.await();

        num_down++;

        lock_down.unlock();
    }

    public void endDownload() {
        lock_down.lock();

        num_down--;

        if (num_down<MAX_DOWN) cond_down.signalAll(); // acho que pode ser só um signal

        lock_down.unlock();
    }


    public void startUpload() throws InterruptedException {
        lock_up.lock();

        while(!(num_up<MAX_UP)) cond_up.await();

        num_up++;

        lock_up.unlock();
    }

    public void endUpload() {
        lock_up.lock();

        num_up--;

        if (num_up<MAX_UP) cond_up.signalAll(); // acho que pode ser só um signal

        lock_up.unlock();
    }



}
