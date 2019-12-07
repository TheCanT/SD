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
            AccessData ad = new AccessData();



            in = new BufferedReader(new InputStreamReader(cli_socket.getInputStream()));

            PrintWriter out = new PrintWriter(cli_socket.getOutputStream());

            String cli_resposta = null;
            System.out.println(" ----  Outro Cliente  ---- ");
            cli_resposta = in.readLine();

            String [] splited = null;
            while (cli_resposta!= null){
                System.out.println(cli_resposta);
                splited = cli_resposta.split(" ");
                try{
                    if(splited[0].equals("registar")){
                        ad.register(splited[1],splited[2]);
                    }else
                    if(splited[0].equals("login")){
                        ad.login(splited[1],splited[2]);
                    }else
                    if(splited[0].equals("logout")){
                        ad.logout(splited[1]);
                    }
                    //else out.println("Não existe comando: "+cli_resposta);
                    out.println(cli_resposta);
                }
                catch(Exception e){
                    out.println(e.getMessage());
                }
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
