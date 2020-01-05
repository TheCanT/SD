package ClientDev;

import java.io.BufferedReader;
import java.io.IOException;

public class ClientIn implements Runnable {
    private BufferedReader in;
    private boolean online;

    ClientIn(BufferedReader in) {
        this.in = in;
        this.online = false;
    }

    synchronized boolean isOnline() {
        return online;
    }

    private synchronized void setOnline(boolean online) {
        this.online = online;
    }

    @Override
    public void run() {
        String server_response = "";

        try {
            while (!server_response.equals("quit")) {
                server_response = in.readLine();
                if (!online && server_response.contains("Successful")) {
                    setOnline(true);
                }
                if (online && server_response.contains("Logout Successful.")) {
                    setOnline(false);
                }

                System.out.println(" --- --- --- --- --- --- --- --- \n");

                for (String s : server_response.split("»")) System.out.println(s);

                System.out.println("\n --- --- --- --- --- --- --- --- \n\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
