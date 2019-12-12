package ServerDev;


import Exceptions.ExceptionLogin;
import Exceptions.ExceptionLogout;
import Exceptions.ExceptionRegister;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ServerConnection implements Runnable{
    private Socket client_socket;
    private ServerModel server_model;
    private String user_logged_in;

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

    private void downloadController(String[] splited_string, PrintWriter pw) {
        // Do it when the client end is prepared
    }


    private void uploadController(String[] splited_string,  BufferedReader br) {
        // Do it when the client end is prepared
    }

    private String searchController(String[] splited_string) {
        Set<String> tags = new HashSet<>();
        if(splited_string.length == 2){
            String [] tags_split = splited_string[1].split("«");
            for (String tag : tags_split) tags.add(tag);
            return server_model.searchByTags(tags).toString();
        }
        return "No Musics Found With Those Tags.";
    }

    public String parseInteraction(String [] splited_string, PrintWriter pw,  BufferedReader br) throws Exception {
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
                uploadController(splited_string, br);
                return "Upload Completed.";


            case "download":
                downloadController(splited_string, pw);
                return "Download Completed, Check Out More Of Our Music.";


            case "search":
                return searchController(splited_string);


            case "quit":
                throw new Exception("quit");


            default:
                return "FUCK: "+ Arrays.toString(splited_string);
        }
    }

    @Override
    public void run() {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(client_socket.getInputStream()));
            PrintWriter out = new PrintWriter(client_socket.getOutputStream());
            String cli_resposta;
            cli_resposta = in.readLine();
            String [] splited;
            String interaction_output;

            while (cli_resposta!= null){
                splited = cli_resposta.split(" ");

                try{
                    interaction_output = parseInteraction(splited,out,in);
                    out.println(interaction_output);
                }
                catch(Exception e){
                    out.println(e.getMessage());
                }
                out.flush();

                cli_resposta = in.readLine();
            }

            client_socket.shutdownInput();
            client_socket.shutdownOutput();
            client_socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
