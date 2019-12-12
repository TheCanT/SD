package ServerDev;

import Exceptions.*;
import ServerDev.ServerData.Music;
import ServerDev.ServerData.ParseFich;
import ServerDev.ServerData.User;
import ServerDev.ServerThroughput.TransferControl;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class ServerModel {

    private Map<String, User> users;
    private ReentrantLock lock_users;


    public ServerModel() throws IOException {
        this.users = ParseFich.loadUsers("/home/gonca/Desktop/test_user");
        this.lock_users = new ReentrantLock();

        this.musics = ParseFich.loadMusicas("/home/gonca/Desktop/test_music");
        this.lock_musics = new ReentrantLock();
        this.transfer_control = new TransferControl();

    }

    /**
     *
     * @param user_in
     * @param pass_in
     * @throws ExceptionLogin
     */
    public void login(String user_in, String pass_in) throws ExceptionLogin {

        lock_users.lock();

        if(users.containsKey(user_in)){

            User user = users.get(user_in);

            user.lockUser();
            lock_users.unlock();

            if(user.getLogged()){
                user.unlockUser();
                throw new ExceptionLogin("Already Logged In.");
            }
            else{
                if(pass_in.equals(user.getPassword())){
                    user.setLogged(true);
                    user.unlockUser();
                }
                else{
                    user.unlockUser();
                    throw new ExceptionLogin("Wrong Password.");
                }
            }
        }
        else{
            lock_users.unlock();
            throw new ExceptionLogin("Account Does Not Exist.");
        }
    }

    /**
     *
     * @param user_logged
     * @throws ExceptionLogout
     */
    public void logout(String user_logged) throws ExceptionLogout {
        lock_users.lock();

        if(users.containsKey(user_logged) && users.get(user_logged).getLogged()){
            User user = users.get(user_logged);

            user.lockUser();
            lock_users.unlock();

            if(user.getNumCurrentTransfers() != 0){
                throw new ExceptionLogout("You Can Not Logout With Transfers Remaining.");
            }

            user.setLogged(false);

            user.unlockUser();
        }
        else{
            lock_users.unlock();
            throw  new ExceptionLogout("You Are Not Logged In.");
        }
    }

    /**
     *
     * @param user_reg
     * @param pass_reg
     * @throws ExceptionRegister
     */
    public void register(String user_reg, String pass_reg) throws ExceptionRegister {
        lock_users.lock();

        if(users.containsKey(user_reg)){
            lock_users.unlock();
            throw new ExceptionRegister("Account Already Exists.");
        }

        users.put(user_reg,new User(user_reg,pass_reg));
        lock_users.unlock();
    }



    private Map<String, Music> musics;
    private ReentrantLock lock_musics;
    private TransferControl transfer_control;


    public void upload(String name_upload, String title_upload, String year_upload,
                       Collection<String> tags_upload) throws ExceptionUpload {
        lock_musics.lock();

        if(musics.containsKey(Music.tryKey(name_upload,title_upload,year_upload))){
            Music music = musics.get(Music.tryKey(name_upload,title_upload,year_upload));

            music.lockMusic();
            lock_musics.unlock();

            music.unlockMusic();
            throw new ExceptionUpload("This Music Already Exists, Your Account Was Added As Owner.");
        }

        Collection<String> owners = new HashSet<>();
        Music music = new Music(name_upload,title_upload,Integer.parseInt(year_upload),tags_upload);

        music.lockMusic();
        lock_musics.unlock();

        try {
            transfer_control.startUpload();

            //transferencia.

            transfer_control.endUpload();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        music.unlockMusic();
    }

    public void download(String input, boolean download_by_key) throws ExceptionDownload {
        if(download_by_key){
            this.downloadByKey(input);
        }
        else {
            this.downloadByTitleArtistYear(input);
        }
    }

    private void downloadByKey(String music_key) throws ExceptionDownload {
        lock_musics.lock();
        if(musics.containsKey(music_key)){
            Music music = musics.get(music_key);

            music.lockMusic();
            lock_musics.unlock();

            music.addReader();
            music.unlockMusic();

            try {
                transfer_control.startDownload();

                //transferencia

                transfer_control.endDownload();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            music.lockMusic();
            music.takeReader();
            music.unlockMusic();
        }
        else{
            lock_musics.unlock();
            throw new ExceptionDownload("This Key Is Invalid, Check The Spelling.");
        }
    }


    private void downloadByTitleArtistYear(String music_parameters) throws ExceptionDownload {
        String [] parameter = music_parameters.split(" ");

        if(parameter.length == 3){
            String try_key = Music.tryKey(parameter[0],parameter[1],parameter[2]);

            downloadByKey(try_key);
        }
        else{
            throw new ExceptionDownload("The Parameters Were Not Correctly Filled.");
        }

    }

    public void search(){

    }

    public void delete(){

    }

}
