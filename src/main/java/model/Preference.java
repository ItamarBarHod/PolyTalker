package model;

import jakarta.persistence.*;

@Entity
public class Preference {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(length = 10)
    private String locale;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    @Override
    public String toString() {
        return "Preference{" +
                "id=" + id +
                ", locale='" + locale + '\'' +
                '}';
    }
}
