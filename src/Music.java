import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.locks.ReentrantLock;

public class Music {
    private String title;
    private String artist;
    private Integer year;

    private Collection<String> tags;
    private Collection<String> owners;

    private ReentrantLock lock_music;


    public Music(String title, String artist, Integer year, Collection<String> tags, Collection<String> owners) {
        this.title = title;
        this.artist = artist;
        this.year = year;

        this.setTags(tags);
        this.setOwners(owners);

        this.lock_music = new ReentrantLock();
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public void setOwners(Collection<String> owners) {
        this.owners = new HashSet<>(owners);
    }

    public void setTags(Collection<String> tags) {
        this.tags = new HashSet<>(tags);
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public Integer getYear() {
        return year;
    }

    public Collection<String> getOwners() {
        return new HashSet<>(owners);
    }

    public Collection<String> getTags() {
        return new HashSet<>(tags);
    }


    public static String tryKey(String name, String title, String year){
        return name+year+title;
    }

    public String getKey(){
        return this.getArtist()+this.getYear()+this.getTitle();
    }


    public void lockMusic(){
        lock_music.lock();
    }

    public void unlockMusic(){
        lock_music.unlock();
    }


    public void addOwner(String user){
        owners.add(user);
    }

    public void deleteOwner(String user){
        owners.remove(user);
    }
}
