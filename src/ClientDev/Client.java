package ClientDev;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client implements Runnable {

    public static void main(String[] args){
        try {
            Socket s = new Socket("localhost",12345);

            PrintWriter out = new PrintWriter(s.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));

            Scanner scan = new Scanner(System.in);

            String cli_ter ;
            String server_response = "";

            while(!server_response.equals("quit") && scan.hasNextLine()){
                cli_ter = scan.nextLine();

                out.println(cli_ter);
                out.flush();

                server_response = in.readLine();

                System.out.println(" --- --- "+cli_ter+" --- --- \n\n"
                                         + server_response +
                                 "\n\n --- --- "+cli_ter+" --- --- \n");
            }

            s.shutdownOutput();
            s.shutdownInput();
            s.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

    }
}
