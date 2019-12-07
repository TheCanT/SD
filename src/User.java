import java.util.concurrent.locks.ReentrantLock;

public class User {

    private String username;
    private String password;

    private boolean logged;

    private ReentrantLock lock;

    private int upload;
    private int download;

    public User(String name, String pass){
        username = name;
        password = pass;

        logged = false;

        lock = new ReentrantLock();

        upload = 0;
        download = 0;
    }


    public int getUpload() {
        return upload;
    }

    public int getDownload() {
        return download;
    }

    public void setDownload(int download) {
        this.download = download;
    }

    public void setUpload(int upload) {
        this.upload = upload;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean getLogged() {
        return logged;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLogged(boolean logged) {
        this.logged = logged;
    }

    public void lockUser(){
        lock.lock();
    }

    public void unlockUser(){
        lock.unlock();
    }

    public int getNumCurrentTransfers(){
        return upload+download;
    }

}
