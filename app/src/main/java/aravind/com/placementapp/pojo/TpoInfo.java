package aravind.com.placementapp.pojo;

public class TpoInfo {
    private String tpoid;
    private String tponame;

    public TpoInfo(String tpoid, String tponame) {
        this.tpoid = tpoid;
        this.tponame = tponame;
    }

    public String getTpoid() {
        return tpoid;
    }

    public String getTponame() {
        return tponame;
    }
}
