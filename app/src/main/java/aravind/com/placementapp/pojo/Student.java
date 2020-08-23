package aravind.com.placementapp.pojo;

public class Student extends User {

    public String branch;
    public float percentage;

    public Student() {
    }

    public Student(String id, String name, String password, int type, String branch, float percentage) {
        super(id, name, password, type);
        this.branch = branch;
        this.percentage = percentage;
    }

    public Student(String id, String name, String branch, float percentage) {
        super(id, name);
        this.branch = branch;
        this.percentage = percentage;
    }

    public String getBranch() {
        return branch;
    }

    public float getPercentage() {
        return percentage;
    }
}
