package ClientDev;

import Exceptions.ExceptionDownload;
import Exceptions.ExceptionUpload;
import Requests.Request;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client{


    private static boolean parseUserRequest(String request, PrintWriter out, BufferedReader in)
            throws ExceptionUpload, ExceptionDownload {
        if(request.startsWith("upload")){
            uploadRequest(request,out);
            System.out.println("The Upload Has Started.");
            return true;
        }
        if(request.startsWith("download")){
            downloadRequest(request,in);
            System.out.println("The Download Has Started.");
            return true;
        }
        return false;
        //return request;
    }

    //deixei o in pq evetualmente se for para fazer o cenas dos tickets
    //esta mal
    private static void downloadRequest(String request, BufferedReader in) throws ExceptionDownload {
        System.out.println("Enter The Name The File Should Have :");

        Scanner scan = new Scanner(System.in);
        String path = null;

        if(scan.hasNextLine()) {
            path = scan.nextLine();
            ClientRequest r = new ClientRequest(request, "/home/gonca/Downloads/"+path);
            Thread th = new Thread(r);
            th.start();
        }
    }

    //deixei o out pq evetualmente se for para fazer o cenas dos tickets
    //esta mal
    private static void uploadRequest(String request, PrintWriter out) throws ExceptionUpload {
        System.out.println("Enter The Path Of The File You Want To Upload :");

        Scanner scan = new Scanner(System.in);
        String path = null;

        if(scan.hasNextLine()){
            path = scan.nextLine();

            ClientRequest r = new ClientRequest(request,path);

            Thread th = new Thread(r);
            th.start();
        }
    }

    public static void main(String[] args){
        try {
            Socket s = new Socket("localhost",12345);

            PrintWriter out = new PrintWriter(s.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));

            Scanner scan = new Scanner(System.in);

            String client_input;
            String client_request;
            String server_response = "";

            while(!server_response.equals("quit") && scan.hasNextLine()){
                client_input = scan.nextLine();

                try {
                    if (!parseUserRequest(client_input,out,in)){
                        client_request = client_input;
                        out.println(client_request);
                        out.flush();

                        server_response = in.readLine();
                        System.out.println(" --- --- --- --- --- --- --- --- \n"
                                + server_response +
                                "\n --- --- --- --- --- --- --- --- \n\n");
                    }
                }
                catch (ExceptionUpload | ExceptionDownload e) {
                    System.out.println(e.getMessage());
                }
            }

            s.shutdownOutput();
            s.shutdownInput();
            s.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
