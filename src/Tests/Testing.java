package Tests;

import ClientDev.ClientRequest;
import Exceptions.ExceptionDownload;
import Exceptions.ExceptionUpload;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Testing implements Runnable{


    private  Socket sd = null;
    public Testing(Socket s) {
        sd = s;
    }

    private static boolean parseUserRequest(String request, PrintWriter out, BufferedReader in, BufferedReader scan)
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
        //return request;
    }

    //deixei o in pq evetualmente se for para fazer o cenas dos tickets
    //esta mal
    private static void downloadRequest(String request, BufferedReader in, BufferedReader scan) throws ExceptionDownload, IOException {
        System.out.println("Enter The Name The File Should Have :");

        //Scanner scan = new Scanner(System.in);
        String path = null;

            path = scan.readLine();
        if(path.length()>0) {
        File new_file = new File("/home/gonca/Downloads/"+path);
            ClientRequest r = new ClientRequest(request, "/home/gonca/Downloads/"+path);
            Thread th = new Thread(r);
            th.start();

        }
    }

    //deixei o out pq evetualmente se for para fazer o cenas dos tickets
    //esta mal
    private static void uploadRequest(String request, PrintWriter out, BufferedReader scan) throws ExceptionUpload, IOException {
        System.out.println("Enter The Path Of The File You Want To Upload :");

       // Scanner scan = new Scanner(scan);
        String path = null;

                path = scan.readLine();
        if(path.length()>0){

            ClientRequest r = new ClientRequest(request,path);

            Thread th = new Thread(r);
            th.start();
        }
    }

    private int k=1;

    @Override
    public void run() {
        try {
            if(sd==null) sd = new Socket("localhost",12345);
            BufferedReader in = new BufferedReader(new InputStreamReader(sd.getInputStream()));


            if(k==1) {
                PrintWriter out = new PrintWriter(sd.getOutputStream());
                Testing l = new Testing(sd); l.k = 2; Thread y = new Thread(l); y.start();

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
                Thread.sleep(4000);
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

        for (int i = 0; i < 10; i++) {
            ths.add(new Thread(new Testing(null)));
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
/*
NAO SEI PORQUE MAS OS DOWNLOADS NESTA CLASSE DE TESTA NAO FUNFA
 */
}
