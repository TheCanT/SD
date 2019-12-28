package ServerDev;


import Exceptions.*;
import ServerDev.ServerData.ServerModel;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ServerConnection implements Runnable{
    private Socket client_socket;
    private ServerModel server_model;
    private String user_logged_in;

    private BufferedReader in;
    private PrintWriter out;

    public ServerConnection(Socket s, ServerModel model) {
        client_socket = s;
        server_model = model;
        user_logged_in = null;
    }

    private void loginController(String[] splited_string) throws ExceptionLogin {
        if(splited_string.length==3){
            if(user_logged_in==null){
                server_model.login(splited_string[1],splited_string[2]);
                user_logged_in = splited_string[1];
            }
            else throw new ExceptionLogin("You Are Already Logged In As "+user_logged_in+".");
        }
        else throw new ExceptionLogin("Incorrect Input (eg. login username password).");
    }

    private void logoutController(String[] splited_string) throws ExceptionLogout {
        if(splited_string.length==2){
            if (user_logged_in != null) {
                server_model.logout(splited_string[1]);
                user_logged_in = null;
            } else throw new ExceptionLogout("You Are Not Logged In.");
        }
        else throw new ExceptionLogout("Incorrect Input (eg. login user).");
    }

    private void registerController(String[] splited_string) throws ExceptionRegister{
        if(splited_string.length==3){
            if(user_logged_in==null){
                server_model.register(splited_string[1],splited_string[2]);
            }
            else throw new ExceptionRegister("You Need To Logout In Order To Register An Account.");
        }
        else throw new ExceptionRegister("Incorrect Input (eg. register username password).");
    }

    private void downloadController(String[] splited_string) throws ExceptionDownload, IOException {
            System.out.println(Arrays.toString(splited_string)+" -> "+splited_string.length);
        if(splited_string.length>1){
            if(splited_string[1].equals("-key")){
                server_model.download(splited_string[2],out,true,in,client_socket.getOutputStream());
            }
            else{
                server_model.download(splited_string[1],out,false,in,client_socket.getOutputStream());
            }
        }
        else throw new ExceptionDownload("Incorrect Input (eg. download -key music_key / " +
                "download  music_title«music_artist«music_year).");
    }

    private void uploadController(String[] splited_string) throws ExceptionUpload, IOException {
        if(splited_string.length==5){


            try {
                String s = in.readLine();
                if (s!=null && !s.equals("READY")) throw new ExceptionUpload(s);
            } catch (IOException e) {
                System.out.println("fuck upload");
            }



            server_model.upload(splited_string[1],splited_string[2],splited_string[3],
                    Collections.singleton(splited_string[4]),client_socket.getInputStream(),out);
        }
        else throw new ExceptionUpload("Incorrect Input (eg. upload music_title music_artist " +
                "music_year tag_1«...«tag_n");
    }

    private String searchController(String[] splited_string) {
        Set<String> tags = new HashSet<>();
        if(splited_string.length == 2){
            String [] tags_split = splited_string[1].split("«");
            tags.addAll(Arrays.asList(tags_split));
            return server_model.searchByTags(tags).toString();
        }
        return "Incorrect Input (eg. search tag_1«tag_2«tag_3«...«tag_n).";
    }

    private String parseInteraction(String[] splited_string, PrintWriter pw, BufferedReader br)
            throws ExceptionDownload, ExceptionLogin, ExceptionRegister, ExceptionLogout, ExceptionUpload, IOException {
        switch (splited_string[0]){
            case "login":
                loginController(splited_string);
                return "Login Successful As "+user_logged_in+".";


            case "logout":
                logoutController(splited_string);
                return "Logout Successful.";


            case "register":
                registerController(splited_string);
                return "Register Successful.";


            case "upload":
                uploadController(splited_string);
                return "Upload Completed.";


            case "download":
                downloadController(splited_string);
                return "Download Completed, Check Out More Of Our Music.";


            case "search":
                return searchController(splited_string);


            case "upload_request":
                return "upload_ticket_request";


            case "download_request":
                return "download_ticket_request";


            case "quit":
                return "quit";


            case "help":
                return "NO HELP YET";

            default:
                return Arrays.toString(splited_string) +" -> Try The Command \"help\".";
        }
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(client_socket.getInputStream()));
            out = new PrintWriter(client_socket.getOutputStream());
            String cli_resposta;
            cli_resposta = in.readLine();
            String [] splited;
            String interaction_output;

            while (cli_resposta!= null){
                splited = cli_resposta.split(" ");

                try{
                    System.out.println("SConnection -> "+cli_resposta);
                    interaction_output = parseInteraction(splited,out,in);
                    out.println(interaction_output);
                }
                catch(ExceptionDownload | ExceptionLogin | ExceptionRegister | ExceptionLogout | ExceptionUpload e){
                    out.println(e.getMessage());
                }
                out.flush();

                cli_resposta = in.readLine();
            }

            client_socket.shutdownInput();
            client_socket.shutdownOutput();
            client_socket.close();

            System.out.println("CLOSE CONNECTION");
        } catch (IOException e) {
            System.out.println("CLOSE CONNECTION 2");
            try {
                client_socket.close();
            } catch (IOException ex) {
                System.out.println("CLOSE CONNECTION 3");
            }
        }

    }
}
