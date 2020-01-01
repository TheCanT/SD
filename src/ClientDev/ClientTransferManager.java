package ClientDev;

import Requests.SharedQueue;

public class ClientTransferManager<E extends Runnable> implements Runnable {
    private SharedQueue<E> queue;

    public ClientTransferManager(){
        this.queue = new SharedQueue<E>();
    }

    public void addTransferRequest(E cr) {
        queue.offer(cr);
    }

    private E nextTransferRequest() {
        return queue.poll();
    }

    @Override
    public void run() {
        while (true) {
            E cr = nextTransferRequest();
            Thread upload = new Thread(cr);
            upload.start();
            try { upload.join(); } catch (InterruptedException ignore) {}
        }
    }
}
