package ServerDev;

import ServerDev.ServerData.ServerModel;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server{

    public static void main(String[] args) {
        ServerSocket sv_socket = null;
        ServerModel sv_model = null;
        try {
            sv_socket = new ServerSocket(12345);
            sv_model = new ServerModel();
            while (true) {
                Socket new_cli_socket = sv_socket.accept();

                ServerConnection sc = new ServerConnection(new_cli_socket,sv_model);
                Thread th = new Thread(sc);

                th.start();
            }
        } catch (IOException e) {
        e.printStackTrace();
        }

    }
}
