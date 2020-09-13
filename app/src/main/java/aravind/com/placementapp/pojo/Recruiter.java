package aravind.com.placementapp.pojo;

public class Recruiter {

    private String name;
    private String id;

    public Recruiter(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public Recruiter() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
