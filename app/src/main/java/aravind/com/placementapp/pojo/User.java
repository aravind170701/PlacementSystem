package aravind.com.placementapp.pojo;

public class User {
    public String id;
    public String name;
    public String password;
    public int type;

    public User() {
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", type=" + type +
                '}';
    }

    public User(String id, String name, String password, int type) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.type = type;
    }

    public User(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public int getType() {
        return type;
    }
}
