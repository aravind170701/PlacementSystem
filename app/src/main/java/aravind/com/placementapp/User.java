package aravind.com.placementapp;

class User {
    public int id;
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

    public User(int id, String name, String password, int type) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.type = type;
    }
}
