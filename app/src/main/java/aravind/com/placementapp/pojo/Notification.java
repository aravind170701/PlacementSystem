package aravind.com.placementapp.pojo;

public class Notification implements Comparable<Notification> {

    private String title;
    private String message;
    private String timestamp;
    private long key;

    public Notification(String title, String message, String timestamp, long key) {
        this.title = title;
        this.message = message;
        this.timestamp = timestamp;
        this.key = key;
    }

    public Notification(String title, String message, String timestamp) {
        this.title = title;
        this.message = message;
        this.timestamp = timestamp;
    }

    public Notification() {
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public long getKey() {
        return key;
    }

    public void setKey(long key) {
        this.key = key;
    }


    @Override
    public int compareTo(Notification o) {
        if (this.getKey() == o.getKey()) {
            return 0;
        } else if (this.getKey() < o.getKey()) {
            return 1;
        } else {
            return -1;
        }
    }
}
