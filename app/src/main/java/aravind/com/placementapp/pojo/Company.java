package aravind.com.placementapp.pojo;

public class Company {
    private String companyid;
    private String companyname;
    private String companydescription;
    private String companyskills;
    private String companyeligibility;

    public Company(String companyid, String companyname, String companydescription, String companyskills, String companyeligibility) {
        this.companyid = companyid;
        this.companyname = companyname;
        this.companydescription = companydescription;
        this.companyskills = companyskills;
        this.companyeligibility = companyeligibility;
    }

    public String getCompanyname() {
        return companyname;
    }

    public String getCompanyid() {
        return companyid;
    }

    public String getCompanydescription() {
        return companydescription;
    }

    public String getCompanyskills() {
        return companyskills;
    }

    public String getCompanyeligibility() {
        return companyeligibility;
    }
}
