package ClientDev;

import ServerDev.ServerNotifications.SharedQueue;

public class ClientDownloadManager implements Runnable {
    private SharedQueue<ClientRequest> queue_downloads;

    public ClientDownloadManager(){
        this.queue_downloads = new SharedQueue<>();
    }

    public void addDownloadRequest(ClientRequest cr) {
        queue_downloads.offer(cr);
    }

    private ClientRequest nextDownloadRequest() {
        return queue_downloads.poll();
    }

    @Override
    public void run() {
        while (true){
                ClientRequest cr = nextDownloadRequest();
            Thread download = new Thread(cr);
            download.start();
            try { download.join(); } catch (InterruptedException ignore) {}
        }
    }
}
