package ServerDev.ServerData;

import java.util.concurrent.locks.ReentrantLock;

public class User {

    private String username;
    private String password;

    private boolean logged;

    private ReentrantLock lock;

    public User(String name, String pass){
        username = name;
        password = pass;

        logged = false;

        lock = new ReentrantLock();
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
}
