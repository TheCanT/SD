package ServerDev.ServerNotifications;

import java.time.LocalDateTime;

public class Notification implements Comparable {
    private String message;
    private LocalDateTime time_stamp;

    @Override
    public String toString() {
        return "["+time_stamp+"] : "+message;
    }

    public Notification(String message) {
        this.message = message;
        this.time_stamp = LocalDateTime.now();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimeStamp() {
        return time_stamp;
    }

    public void setTimeStamp(LocalDateTime time_stamp) {
        this.time_stamp = time_stamp;
    }

    @Override
    public int compareTo(Object o) {
        Notification other = (Notification) o;
        return this.getTimeStamp().compareTo(other.getTimeStamp());
    }
}
