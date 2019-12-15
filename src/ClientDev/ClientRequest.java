package ClientDev;

import Exceptions.ExceptionUpload;
import Requests.Request;

import java.io.*;
import java.net.Socket;

public class ClientRequest implements Runnable {
    private String string_request;
    private String path;

    public ClientRequest(String string_request, String path) {
        this.string_request = string_request;
        this.path = path;
    }

    @Override
    public void run() {
        try {
            Socket socket_request = new Socket("localhost",12345);
            PrintWriter out = new PrintWriter(socket_request.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(socket_request.getInputStream()));



            BufferedWriter bw = null;
            BufferedReader br = null;


            System.out.println("CLIENT REQUEST 1");

            if(string_request.startsWith("upload")){
                bw = new BufferedWriter(out);
                br = new BufferedReader(new FileReader(path));

            }else
            if(string_request.startsWith("download")){
                bw = new BufferedWriter(new FileWriter(path));
                br = in;
            }

            System.out.println("CLIENT REQUEST 2");

            Request request = new Request(bw,br);

            out.println(string_request);
            out.flush();

            request.transferRequest();

            System.out.println("CLIENT REQUEST 3");

            //FECHAR O OUT PUT PARA O OUTRO NAO FICAR Á ESPERA DE ALGO.
            socket_request.shutdownOutput();

            System.out.println("/\\ /\\ /\\ Your Request \""+in.readLine()+"\" Is Completed! /\\ /\\ /\\");


            socket_request.shutdownInput();
            socket_request.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
