import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor implements Runnable {
    private String path;

    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(35353);
        Socket s = ss.accept();
        BufferedReader is = new BufferedReader(new InputStreamReader(s.getInputStream()));
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(is.readLine() + "----acabou sv");
    }

    @Override
    public void run() {

    }
}
