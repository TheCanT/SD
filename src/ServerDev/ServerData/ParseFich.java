package ServerDev.ServerData;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static java.lang.Integer.parseInt;

public class ParseFich {

    /*
    * INDEX OF DATA IN SPLIT STRINGS FROM USERS
    * */
    private static final int USERNAME = 0;
    private static final int PASSWORD = 1;


    /*
    * INDEX OF DATA IN SPLIT STRINGS FROM MUSICS
    * */
    private static final int TITULO = 0;
    private static final int ARTISTA = 1;
    private static final int ANO = 2;
    private static final int ETIQUETAS = 3;
    private static final int DONOS = 4;



    public static void saveUsers(Map<String, User> users, String path) throws IOException {
        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(path)));

        for(User user : users.values()){
            pw.println(user.getUsername()+"«"+ user.getPassword());
        }

        pw.flush();
        pw.close();
    }

    public static Map<String,User> loadUsers(String path) throws IOException {
        Map<String,User> users = new HashMap<>();
        String[] user_split = null;

        List<String> list_of_users = Files.readAllLines(Paths.get(String.valueOf((new File(path)))));

        for(String user : list_of_users){
            user_split = user.split("«");
            users.put(user_split[USERNAME],new User(user_split[USERNAME],user_split[PASSWORD]));
        }

        return users;
    }


    public static void saveMusicas(Map<String, Music> musicas, String path) throws IOException {
        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(path)));
        StringBuilder sb = null;

        for(Music musica : musicas.values()){
            sb = new StringBuilder();
            sb.append(musica.getTitle()).append("«")
                    .append(musica.getArtist()).append("«")
                    .append(musica.getYear()).append("«");

            for(String etiqueta : musica.getTags())
                sb.append(etiqueta).append("»");
        }

        pw.flush();
        pw.close();
    }

    public static Map<String, Music> loadMusicas(String path) throws IOException {
        Map<String, Music> musicas = new HashMap<>();
        String[] music_split = null;
        String[] etiquetas_split = null;
        String[] donos_split = null;
        Set<String> etiquetas = null;
        Set<String> donos = null;

        List<String> list_of_users = Files.readAllLines(Paths.get(String.valueOf((new File(path)))));

        for(String user : list_of_users){
            music_split = user.split("«");

            etiquetas_split = music_split[ETIQUETAS].split("»");
            etiquetas = new HashSet<>(Arrays.asList(etiquetas_split));

            donos_split = music_split[DONOS].split("»");
            donos = new HashSet<>(Arrays.asList(donos_split));

            musicas.put(music_split[TITULO]+parseInt(music_split[ANO])+music_split[ARTISTA],
                    new Music(music_split[TITULO],
                            music_split[ARTISTA],
                            parseInt(music_split[ANO]),
                            etiquetas));
        }

        return musicas;
    }


    /* TESTING
    *  THE FILES MUST REMAIN THE SAME WAY
    * */
    public static void main(String[] args) {

        try{
            Map<String, Music> mm = ParseFich.loadMusicas("/home/gonca/Desktop/test_music");
            Map<String, User> mu = ParseFich.loadUsers("/home/gonca/Desktop/test_user");

            ParseFich.saveMusicas(mm,"/home/gonca/Desktop/test_music");
            ParseFich.saveUsers(mu,"/home/gonca/Desktop/test_user");

        }
        catch (Exception e){
            System.out.println(e);
        }

    }
}
