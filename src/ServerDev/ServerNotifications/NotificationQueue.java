package ServerDev.ServerNotifications;

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantLock;

public class NotificationQueue{
    private Queue<Notification> queue;
    private ReentrantLock queue_lock;

    public NotificationQueue() {
        this.queue = new PriorityQueue<>();
        this.queue_lock = new ReentrantLock();
    }

    public boolean add(Notification n) {
        this.queue_lock.lock();

        boolean bool = queue.add(n);

        this.queue_lock.unlock();
        return bool;
    }


    public boolean offer(Notification n) {
        this.queue_lock.lock();

        boolean bool = queue.offer(n);

        this.queue_lock.unlock();
        return bool;
    }


    public Notification remove() {
        return queue.remove();
    }


    public Notification poll() {
        return queue.poll();
    }


    public Notification element() {
        return queue.element();
    }


    public Notification peek() {
        return queue.peek();
    }
}
