import java.io.*;
import java.net.Socket;

public class ThServer implements Runnable{
    Socket cli_socket;

    public ThServer(Socket s) {
        cli_socket = s;
    }

    @Override
    public void run() {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(cli_socket.getInputStream()));

            PrintWriter out = new PrintWriter(cli_socket.getOutputStream());

            String cli_resposta = null;
            System.out.println(" ----  Outro Cliente  ---- ");
            cli_resposta = in.readLine();

            while (cli_resposta!= null){

                FileWriter out_file = new FileWriter(new File("/home/gonca/Desktop/test2.mp3"),true);
                out_file.append(cli_resposta+"\n");
                out_file.flush();

                System.out.println("Mensagem : " + cli_resposta);

                out.println(cli_resposta+" 2.0");
                out.flush();

                cli_resposta = in.readLine();
            }

            cli_socket.shutdownInput();
            cli_socket.shutdownOutput();
            cli_socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
