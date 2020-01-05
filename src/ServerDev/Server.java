package ServerDev;

import Requests.Proprities;
import Requests.SharedQueue;
import ServerDev.ServerNotifications.ServerNotifier;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {


    public static void main(String[] args) {
        ServerSocket sv_socket;
        ServerModel sv_model;
        ServerNotifier sv_notifier;
        try {
            sv_socket = new ServerSocket(Proprities.PORT_NUMBER);
            sv_notifier = new ServerNotifier(new SharedQueue<>());
            sv_model = new ServerModel();
            Thread th_notifier = new Thread(sv_notifier);
            th_notifier.setPriority(Thread.MIN_PRIORITY);
            th_notifier.start();
            while (true) {
                Socket new_cli_socket = sv_socket.accept();

                ServerConnection sc = new ServerConnection(new_cli_socket, sv_model, sv_notifier);
                Thread th = new Thread(sc);

                th.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
