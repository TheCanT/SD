package ClientDev;

import Requests.Proprities;

import java.io.*;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        Socket s;
        try {
            s = new Socket(Proprities.IP_ADDRESS, Proprities.PORT_NUMBER);
            PrintWriter out = new PrintWriter(s.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));

            ClientIn ci = new ClientIn(in);
            ClientOut co = new ClientOut(out, ci);

            Thread th_ci = new Thread(ci);
            Thread th_co = new Thread(co);

            th_ci.start();
            th_co.start();

            th_ci.join();
            th_co.join();

            s.shutdownOutput();
            s.shutdownInput();
            s.close();
        } catch (IOException e) {
            System.out.println("Error Connecting To The Server.");
        } catch(InterruptedException ignore) {}
    }
}
