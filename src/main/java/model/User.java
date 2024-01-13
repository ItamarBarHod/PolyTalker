package model;

import jakarta.persistence.*;

@Entity
public class User {

    @Id
    @Column(length = 45)
    private String userName;

    @Column(length = 45)
    private String nickName;

    private int preference;

    public User() {}

    public User(String userName, String nickName, int preference) {
        this.userName = userName;
        this.nickName = nickName;
        this.preference = preference;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getPreference() {
        return preference;
    }

    public void setPreference(int preference) {
        this.preference = preference;
    }

    @Override
    public String toString() {
        return "User{" +
                ", userName='" + userName + '\'' +
                ", nickName='" + nickName + '\'' +
                ", preference=" + preference +
                '}';
    }
}
