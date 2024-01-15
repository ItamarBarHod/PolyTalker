package model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserID implements Serializable {
    @Column(length = 32)
    private String userName;

    private long guildNumber;

    public UserID(long guildNumber, String userName) {
        this.userName = userName;
        this.guildNumber = guildNumber;
    }

    public UserID() {

    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getGuildNumber() {
        return guildNumber;
    }

    public void setGuildNumber(Long guildNumber) {
        this.guildNumber = guildNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserID)) return false;
        UserID userID = (UserID) o;
        return Objects.equals(getUserName(), userID.getUserName()) && Objects.equals(getGuildNumber(), userID.getGuildNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserName(), getGuildNumber());
    }

    @Override
    public String toString() {
        return guildNumber + "_" + userName;
    }
}
