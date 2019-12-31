package ServerDev.ServerNotifications;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.locks.ReentrantLock;

public class ServerNotifier implements Runnable{
    private NotificationQueue notifications;
    private Collection<PrintWriter> clients;

    private ReentrantLock lock_clients;

    public ServerNotifier(NotificationQueue notifications) {
        this.notifications = notifications;
        this.clients = new ArrayList<>();
        lock_clients = new ReentrantLock();
    }

    public void addNotification(Notification n){
        notifications.offerNotification(n);
    }

    private Notification nextNotification(){
        Notification n = notifications.pollNotification();
        return n;
    }

    public void removeClientNotifier(PrintWriter out){
        lock_clients.lock();
        clients.remove(out);
        lock_clients.unlock();
    }

    public void addClientNotifier(PrintWriter out){
        lock_clients.lock();
        clients.add(out);
        lock_clients.unlock();
    }

    public void run() {
        while(true){
            Notification not = nextNotification();
            lock_clients.lock();
            for(PrintWriter client : clients){
                client.println("Notification >>> "+not.toString());
                client.flush();
            }
            lock_clients.unlock();
        }
    }
}
