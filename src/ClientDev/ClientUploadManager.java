package ClientDev;

import ServerDev.ServerNotifications.SharedQueue;

public class ClientUploadManager implements Runnable {
    private SharedQueue<ClientRequest> queue_uploads;
//    private RequestControl request_uploads;

    public ClientUploadManager(){
        this.queue_uploads = new SharedQueue<>();
//        this.request_uploads = new RequestControl(1);
    }

    public void addUploadRequest(ClientRequest cr) {
        queue_uploads.offer(cr);
    }

    private ClientRequest nextUploadRequest() {
        return queue_uploads.poll();
    }

    @Override
    public void run() {
        while (true) {
            ClientRequest cr = nextUploadRequest();
            Thread upload = new Thread(cr);
            upload.start();
            try { upload.join(); } catch (InterruptedException ignore) {}
        }
    }
}
