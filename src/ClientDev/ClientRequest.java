package ClientDev;

import Exceptions.ExceptionDownload;
import Exceptions.ExceptionUpload;
import Requests.Request;

import java.io.*;
import java.net.Socket;

public class ClientRequest implements Runnable {
    private String string_request;
    private String path;

    private BufferedReader in;
    private PrintWriter out;


    private BufferedWriter bw;
    private BufferedReader br;


    public ClientRequest(String string_request, String path) {
        this.string_request = string_request;
        this.path = path;
    }

    private void downloadHandler() throws IOException {
        bw = new BufferedWriter(new FileWriter(path));
        br = in;
    }

    private void uploadHandler() throws FileNotFoundException {
        bw = new BufferedWriter(out);
        br = new BufferedReader(new FileReader(path));
    }

    private void downloadStartTransfer() throws IOException, ExceptionDownload {
        String s = in.readLine();
        if (!s.equals("READY")) throw new ExceptionDownload(s);
        out.println("START");
        out.flush();
    }

    private void uploadStartTransfer() throws IOException, ExceptionUpload {
        out.println("READY");
        out.flush();
        String s = in.readLine();
        if (!s.equals("START")) throw new ExceptionUpload (s);
    }

    @Override
    public void run() {
        try {
            Socket socket_request = new Socket("localhost",12345);
            out = new PrintWriter(socket_request.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket_request.getInputStream()));

            out.println(string_request);
            out.flush();

            if(string_request.startsWith("upload")){
                uploadHandler();
                uploadStartTransfer();
            }else
            if(string_request.startsWith("download")){
                downloadHandler();
                downloadStartTransfer();
            }

            Request request = new Request(bw,br);
            request.transferRequest();


            socket_request.shutdownOutput();

            String aa = in.readLine();

            System.out.println("/\\ /\\ /\\ Your Request \""+(aa==null?"DOWNLOAD" :aa )+"\" Is Completed! /\\ /\\ /\\");


            socket_request.shutdownInput();
            socket_request.close();

        }
        catch (IOException | ExceptionUpload | ExceptionDownload e) {
            System.out.println(e.getMessage());
        }
    }
}
