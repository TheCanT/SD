package ServerDev.ServerNotifications;

import Requests.SharedQueue;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.locks.ReentrantLock;

public class ServerNotifier implements Runnable {
    private SharedQueue<Notification> notifications;

    private Collection<PrintWriter> clients;
    private ReentrantLock lock_clients;

    public ServerNotifier(SharedQueue<Notification> notifications) {
        this.notifications = notifications;
        this.clients = new ArrayList<>();
        lock_clients = new ReentrantLock();
    }

    public void addNotification(Notification n) {
        notifications.offer(n);
    }

    private Notification nextNotification() {
        return notifications.poll();
    }

    public void removeClientNotifier(PrintWriter out) {
        lock_clients.lock();
        clients.remove(out);
        lock_clients.unlock();
    }

    public void addClientNotifier(PrintWriter out) {
        lock_clients.lock();
        clients.add(out);
        lock_clients.unlock();
    }

    public void run() {
        while (true) {
            Notification not = nextNotification();
            lock_clients.lock();
            for (PrintWriter client : clients) {
                synchronized (client) {
                    client.println("\033[38;5;196mNotification >>> " + not.toString() + "\033[0m");
                    client.flush();
                }
            }
            lock_clients.unlock();
        }
    }
}
