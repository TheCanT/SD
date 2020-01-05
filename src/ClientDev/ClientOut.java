package ClientDev;

import Requests.Proprities;

import java.io.PrintWriter;
import java.util.Scanner;

public class ClientOut implements Runnable {
    private PrintWriter out;
    private ClientIn ci;

    private ClientTransferManager<ClientRequest> cdm;
    private ClientTransferManager<ClientRequest> cum;

    ClientOut(PrintWriter out, ClientIn ci) {
        this.out = out;
        this.ci = ci;

        this.cdm = new ClientTransferManager<>();
        this.cum = new ClientTransferManager<>();
    }

    private boolean parseUserRequest(String request) {
        if (request.startsWith("upload")) {
            if(!ci.isOnline()) {
                System.out.println("You Must Be Logged In To Do Uploads.");
                return true;
            }
            uploadRequest(request);
            return true;
        }
        if (request.startsWith("download")) {
            if(!ci.isOnline()) {
                System.out.println("You Must Be Logged In To Do Downloads.");
                return true;
            }
            downloadRequest(request);
            return true;
        }
        return false;
    }


    private void downloadRequest(String request) {
        System.out.println("Enter The Name The File Should Have :");

        Scanner scan = new Scanner(System.in);
        String path = null;

        if(scan.hasNextLine()) {
            path = scan.nextLine();

            ClientRequest r = new ClientRequest(request, Proprities.DOWNLOADS_PATH +path);

            cdm.addTransferRequest(r);
        }
    }


    private void uploadRequest(String request) {
        System.out.println("Enter The Path Of The File You Want To Upload :");

        Scanner scan = new Scanner(System.in);
        String path = null;

        if(scan.hasNextLine()){
            path = scan.nextLine();

            ClientRequest r = new ClientRequest(request,path);

            cum.addTransferRequest(r);
        }
    }

    @Override
    public void run() {
        Thread th_cdm = new Thread(this.cdm);
        Thread th_cum = new Thread(this.cum);

        th_cdm.start();
        th_cum.start();

        Scanner scan = new Scanner(System.in);

        String client_input;
        String client_request;

        out.println("notify");
        out.flush();

        while(scan.hasNextLine()){
            client_input = scan.nextLine();

            if (!parseUserRequest(client_input)){
                client_request = client_input;
                out.println(client_request);
                out.flush();
            }
        }
    }
}
