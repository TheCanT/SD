package Tests;

import ClientDev.ClientRequest;
import ClientDev.ClientTransferManager;
import Exceptions.ExceptionDownload;
import Exceptions.ExceptionUpload;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Testing implements Runnable{
    private  Socket sd;
    private int id;

    private ClientTransferManager<ClientRequest> cdm;
    private ClientTransferManager<ClientRequest> cum;

    private Testing(Socket s, int i) {
        sd = s;
        id = i;

        this.cdm = new ClientTransferManager<>();
        this.cum = new ClientTransferManager<>();
    }

    private boolean parseUserRequest(String request, PrintWriter out, BufferedReader in, BufferedReader scan)
            throws ExceptionUpload, ExceptionDownload, IOException {
        if(request.startsWith("download")){
            downloadRequest(request,in,scan);
            System.out.println("The Download Has Started.");
            return true;
        }
        if(request.startsWith("upload")){
            uploadRequest(request,out,scan);
            System.out.println("The Upload Has Started.");
            return true;
        }
        return false;
    }

    private void downloadRequest(String request, BufferedReader in, BufferedReader scan) throws ExceptionDownload, IOException {
        System.out.println("Enter The Name The File Should Have :");

        String path = null;

            path = scan.readLine();
        if(path.length()>0) {
            ClientRequest r = new ClientRequest(request, "/home/gonca/Downloads/"+path+"_"+this.id);

            cdm.addTransferRequest(r);
        }
    }

    private void uploadRequest(String request, PrintWriter out, BufferedReader scan) throws ExceptionUpload, IOException {
        System.out.println("Enter The Path Of The File You Want To Upload :");

        String path = null;

        path = scan.readLine();
        if(path.length()>0){

            ClientRequest r = new ClientRequest(request,path);

            cum.addTransferRequest(r);
        }
    }

    private int k=1;

    @Override
    public void run() {
        Thread th_cdm = new Thread(this.cdm);
        Thread th_cum = new Thread(this.cum);

        th_cdm.start();
        th_cum.start();

        try {
            if(sd==null) sd = new Socket("localhost",12345);
            BufferedReader in = new BufferedReader(new InputStreamReader(sd.getInputStream()));


            if(k==1) {
                PrintWriter out = new PrintWriter(sd.getOutputStream());


                out.println("notihuify");
                out.flush();


                Testing l = new Testing(sd, id); l.k = 2; Thread y = new Thread(l); y.start();

                BufferedReader scan = new BufferedReader(new FileReader("/home/gonca/Desktop/testing"));

                String client_input = "  ";
                String client_request = "  ";

                while (client_input != null) {
                    client_input = scan.readLine();

                    try {
                        if (client_input != null && !parseUserRequest(client_input, out, in, scan)) {
                            client_request = client_input;
                            out.println(client_request);
                            out.flush();
                        }
                    } catch (ExceptionUpload | ExceptionDownload e) {
                        System.out.println(e.getMessage());
                    }
                }
                y.join();
                out.close();
                in.close();
                sd.shutdownOutput();
                sd.shutdownInput();
                sd.close();
            }
            else {
                String server_response = "null";
                while (server_response != null && !server_response.equals("quit")) {
                    server_response = in.readLine();
                    System.out.println(" --- --- --- --- --- --- --- --- \n"
                            + server_response +
                            "\n --- --- --- --- --- --- --- --- \n\n");
                }
            }

        } catch (IOException | InterruptedException ignore) {

        }
    }

    public static void main(String[] args) {
        List<Thread> ths = new ArrayList<>();

        for (int i = 0; i < 15; i++) {
            ths.add(new Thread(new Testing(null,i)));
        }

        for (Thread  th : ths) th.start();

        for (Thread  th : ths) {
            try {
                th.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
