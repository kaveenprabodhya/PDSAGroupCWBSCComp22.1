package bsccomp.group.common.models;

public class User {
    int userId;
    String username;

    public User() {
    }

    public User(String username) {
        this.username = username;
    }

    public User(int userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
