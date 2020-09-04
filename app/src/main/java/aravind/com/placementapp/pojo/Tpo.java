package aravind.com.placementapp.pojo;

public class Tpo extends User {

    private String designation;

    public Tpo() {
    }

    public Tpo(String designation) {
        this.designation = designation;
    }

    public Tpo(String id, String name, String password, int type, String designation) {
        super(id, name, password, type);
        this.designation = designation;
    }

    public Tpo(String id, String name, String designation) {
        super(id, name);
        this.designation = designation;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }
}
