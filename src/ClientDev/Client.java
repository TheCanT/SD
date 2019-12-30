package ClientDev;

import Exceptions.ExceptionDownload;
import Exceptions.ExceptionUpload;
import Requests.Request;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client{
/*

    private static BufferedReader in;

    private static boolean parseUserRequest(String request)
            throws ExceptionUpload, ExceptionDownload {
        System.out.println("parse 1 ");
            if (request.startsWith("upload")) {
                if(!online) return true;
                uploadRequest(request);
                System.out.println("The Upload Has Started.");
                return true;
            }
            if (request.startsWith("download")) {
                if(!online) return true;
                downloadRequest(request);
                System.out.println("The Download Has Started.");
                return true;
            }

        System.out.println("parse 3 ");
        return false;
        //return request;
    }

    //deixei o in pq evetualmente se for para fazer o cenas dos tickets
    //esta mal
    private static void downloadRequest(String request) throws ExceptionDownload {
        System.out.println("Enter The Name The File Should Have :");

        Scanner scan = new Scanner(System.in);
        String path = null;

        File new_file = new File("/home/gonca/Downloads/"+path);
        if(scan.hasNextLine()) {
            path = scan.nextLine();
            ClientRequest r = new ClientRequest(request, "/home/gonca/Downloads/"+path);
            Thread th = new Thread(r);
            th.start();

        }
    }

    //deixei o out pq evetualmente se for para fazer o cenas dos tickets
    //esta mal
    private static void uploadRequest(String request) throws ExceptionUpload {
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

    private static boolean online = false;


        try {
            Socket s = new Socket("localhost",12345);

            PrintWriter out = new PrintWriter(s.getOutputStream());
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            Scanner scan = new Scanner(System.in);

            String client_input;
            String client_request;

            while(scan.hasNextLine()){
                client_input = scan.nextLine();

                try {
                    if (!parseUserRequest(client_input,out)){
                        client_request = client_input;
                        out.println(client_request);
                        out.flush();

                    }
                    else{
                        System.out.println("Restricted");
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

    @Override
    public void run() {
        String server_response = "";

        try {
            while(!server_response.equals("quit")) {
                server_response = in.readLine();
                if (!online && server_response.contains("Successful")) online = true;
                System.out.println(" --- --- --- --- --- --- --- --- \n"
                                           + server_response +
                                 "\n --- --- --- --- --- --- --- --- \n\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        }
 */
    public static void main(String[] args){
        Socket s = null;
        try {
            s = new Socket("localhost",12345);
            PrintWriter out = new PrintWriter(s.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));

            ClientIn ci = new ClientIn(in);
            ClientOut co = new ClientOut(out,ci);

            Thread th_ci = new Thread(ci);
            Thread th_co = new Thread(co);

            th_ci.start();
            th_co.start();

            th_ci.join();
            th_co.join();

            s.shutdownOutput();
            s.shutdownInput();
            s.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
