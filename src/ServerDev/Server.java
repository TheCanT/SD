package ServerDev;

import ServerDev.ServerNotifications.NotificationQueue;
import ServerDev.ServerNotifications.ServerNotifier;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server{


    public static void main(String[] args) {
        ServerSocket sv_socket = null;
        ServerModel sv_model = null;
        ServerNotifier sv_notifier = null;
        try {
            sv_socket = new ServerSocket(12345);
            sv_notifier = new ServerNotifier(new NotificationQueue());
            sv_model = new ServerModel();
            Thread th_notifier = new Thread(sv_notifier);
            th_notifier.setPriority(Thread.MIN_PRIORITY);
            th_notifier.start();
            while (true) {
                Socket new_cli_socket = sv_socket.accept();

                ServerConnection sc = new ServerConnection(new_cli_socket,sv_model,sv_notifier);
                Thread th = new Thread(sc);

                th.start();
            }
        } catch (IOException e) {
        e.printStackTrace();
        }

    }
}
