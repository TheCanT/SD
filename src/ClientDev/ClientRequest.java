package ClientDev;

import Exceptions.ExceptionDownload;
import Exceptions.ExceptionUpload;
import Requests.Request;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;

public class ClientRequest implements Runnable,Comparable {
    private Socket socket_request;

    private String string_request;
    private String path;

    private BufferedReader in;
    private PrintWriter out;

    private DataOutputStream bw;
    private DataInputStream br;

    private LocalDateTime time_stamp;


    public ClientRequest(String string_request, String path) {
        this.string_request = string_request;
        this.path = path;
        this.time_stamp = LocalDateTime.now();
    }

    private void downloadHandler() throws IOException {
        bw = new DataOutputStream(new FileOutputStream(path));
        br = new DataInputStream(socket_request.getInputStream());
    }

    private void uploadHandler() throws FileNotFoundException {
        try {
            bw = new DataOutputStream(socket_request.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        br = new DataInputStream(new FileInputStream(path));
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

    private LocalDateTime getTimeStamp() {
        return time_stamp;
    }

    @Override
    public int compareTo(Object o) {
        ClientRequest other = (ClientRequest) o;
        return this.getTimeStamp().compareTo(other.getTimeStamp());
    }

    @Override
    public void run() {
        String aa = null;
        try {
            socket_request = new Socket("localhost",12345);
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

            aa = in.readLine();

            System.out.println("/\\ /\\ /\\ Your Request \""+(aa==null?"DOWNLOAD" :aa )+"\" Is Completed! /\\ /\\ /\\");


            socket_request.shutdownInput();
            socket_request.close();

        }
        catch ( ExceptionDownload e){
            out.println(e.getMessage());
            out.flush();
            try
            {
                Files.deleteIfExists(Paths.get(path));
            }
            catch(IOException ignored) { }
        }
        catch (IOException | ExceptionUpload e) {
            out.println(e.getMessage());
            out.flush();
        }
    }
}
