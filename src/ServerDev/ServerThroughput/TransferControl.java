package ServerDev.ServerThroughput;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class TransferControl {
    private ReentrantLock lock_down;
    private Condition cond_down;
    private int MAX_DOWN;
    private int num_down;


    private ReentrantLock lock_up;
    private Condition cond_up;
    private int MAX_UP;
    private int num_up;


    public TransferControl(int max_download, int max_upload) {
        lock_down = new ReentrantLock();
        cond_down = lock_down.newCondition();
        num_down = 0;
        MAX_DOWN = max_download;

        lock_up = new ReentrantLock();
        cond_up = lock_up.newCondition();
        num_up = 0;
        MAX_UP = max_upload;
    }


    public ReentrantLock getLockDown() {
        return lock_down;
    }

    public void startDownload() throws InterruptedException {
        lock_down.lock();

        while (!(num_down < MAX_DOWN)) cond_down.await();

        num_down++;

        lock_down.unlock();
    }

    public void endDownload() {
        lock_down.lock();

        if (!(num_down < MAX_DOWN)) cond_down.signal();

        num_down--;

        lock_down.unlock();
    }

    public ReentrantLock getLockUp() {
        return lock_up;
    }

    public void startUpload() throws InterruptedException {
        lock_up.lock();

        while (!(num_up < MAX_UP)) cond_up.await();

        num_up++;

        lock_up.unlock();
    }

    public void endUpload() {
        lock_up.lock();

        if (!(num_up < MAX_UP)) cond_up.signal();

        num_up--;

        lock_up.unlock();
    }


}
