package model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class User {

    @EmbeddedId
    private UserID id;

    @Column(length = 32)
    private String nickName;

    private int preference;

    public User() {}

    public User(UserID id, String nickName, int preference) {
        this.id = id;
        this.nickName = nickName;
        this.preference = preference;
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


    public UserID getId() {
        return id;
    }

    public void setId(UserID id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return getPreference() == user.getPreference() && Objects.equals(getId(), user.getId()) && Objects.equals(getNickName(), user.getNickName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getNickName(), getPreference());
    }
}
