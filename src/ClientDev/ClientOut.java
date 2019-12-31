package ClientDev;

import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

public class ClientOut implements Runnable {
    private PrintWriter out;
    private ClientIn ci;

    public ClientOut(PrintWriter out, ClientIn ci) {
        this.out = out;
        this.ci = ci;
    }

    private boolean parseUserRequest(String request) {
        if (request.startsWith("upload")) {
            if(!ci.isOnline()) {
                System.out.println("You Must Be Logged In To Do Uploads/Downloads.");
                return true;
            }
            System.out.println("we here 1");
            uploadRequest(request);
            System.out.println("The Upload Has Started.");
            return true;
        }
        if (request.startsWith("download")) {
            if(!ci.isOnline()) {
                System.out.println("You Must Be Logged In To Do Uploads/Downloads.");
                return true;
            }
            System.out.println("we here 2");
            downloadRequest(request);
            System.out.println("The Download Has Started.");
            return true;
        }
        return false;
    }

    //deixei o in pq evetualmente se for para fazer o cenas dos tickets
    //esta mal
    private void downloadRequest(String request) {
        System.out.println("Enter The Name The File Should Have :");

        Scanner scan = new Scanner(System.in);
        String path = null;

        File new_file = new File("/home/gonca/Downloads/"+path);
        if(scan.hasNextLine()) {
            path = scan.nextLine();
            ClientRequest r = new ClientRequest(request, "/home/gonca/Downloads/"+path);
            Thread th = new Thread(r);
            th.start();

        }
    }

    //deixei o out pq evetualmente se for para fazer o cenas dos tickets
    //esta mal
    private void uploadRequest(String request) {
        System.out.println("Enter The Path Of The File You Want To Upload :");

        Scanner scan = new Scanner(System.in);
        String path = null;

        if(scan.hasNextLine()){
            path = scan.nextLine();

            ClientRequest r = new ClientRequest(request,path);

            Thread th = new Thread(r);
            th.start();
        }
    }

    @Override
    public void run() {
        Scanner scan = new Scanner(System.in);

        String client_input;
        String client_request;

        out.println("notify");
        out.flush();

        while(scan.hasNextLine()){
            client_input = scan.nextLine();

            if (!parseUserRequest(client_input)){
                client_request = client_input;
                out.println(client_request);
                out.flush();
            }
        }
    }
}
