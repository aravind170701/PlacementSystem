package aravind.com.placementapp.pojo;

public class TopPlacedStudents {
    private String name;
    private String company;
    private String salary;
    private String batch;

    public TopPlacedStudents(String name, String company, String salary, String batch) {
        this.name = name;
        this.company = company;
        this.salary = salary;
        this.batch = batch;
    }

    public TopPlacedStudents() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

}
