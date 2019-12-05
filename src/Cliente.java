import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Cliente implements Runnable {

    public static void main(String[] args){
        try {
            Socket s = new Socket("localhost",12345);

            PrintWriter out = new PrintWriter(s.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));

            Scanner scan = new Scanner(System.in);//new FileReader(new File("/home/gonca/Desktop/test.mp3")));
            String cli_ter = "";

            while(scan.hasNextLine() && !cli_ter.equals("quit")){

                cli_ter = scan.nextLine();

                out.println(cli_ter);
                out.flush();

                System.out.println("Resposta do Servidor : " + in.readLine());
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
