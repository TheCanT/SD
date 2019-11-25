import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;
import org.json.*;

public class Servidor implements Runnable {
    private String path;

    public static void main(String[] args) {
        ServerSocket sv_socket = null;
        try {
            sv_socket = new ServerSocket(12345);

            while (true) {
                Socket new_cli_socket = sv_socket.accept();

                ThServer wk = new ThServer(new_cli_socket);
                Thread th = new Thread(wk);

                th.start();
            }
        } catch (IOException e) {
        e.printStackTrace();
    }

}

    @Override
    public void run() {

    }
}
