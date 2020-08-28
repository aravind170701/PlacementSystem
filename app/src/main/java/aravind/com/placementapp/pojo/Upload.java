package aravind.com.placementapp.pojo;

public class Upload {
    public Upload() {
    }

    private String name;
    private String url;

    public Upload(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
