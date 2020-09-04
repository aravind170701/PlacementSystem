package aravind.com.placementapp.pojo;

public class Student extends User {

    public String branch;
    public float percentage;
    public String year;

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Student() {
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }

    public Student(String id, String name, String password, int type, String branch, float percentage) {
        super(id, name, password, type);
        this.branch = branch;
        this.percentage = percentage;
    }

    public Student(String id, String name, String password, int type, String branch, float percentage, String year) {
        super(id, name, password, type);
        this.branch = branch;
        this.percentage = percentage;
        this.year = year;
    }

    public Student(String id, String name, String branch, float percentage) {
        super(id, name);
        this.branch = branch;
        this.percentage = percentage;
    }

    public Student(String id, String name, String branch, String year) {
        super(id, name);
        this.branch = branch;
        this.year = year;
    }

    public String getBranch() {
        return branch;
    }

    public float getPercentage() {
        return percentage;
    }
}
