package model;

import org.example.lib.LanguageManager;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class DatabaseUtil {
    private static final SessionFactory factory = new Configuration()
            .configure("hibernate.cfg.xml")
            .setProperty("hibernate.connection.url", System.getenv("DB_URL"))
            .setProperty("hibernate.connection.username", System.getenv("DB_USER"))
            .setProperty("hibernate.connection.password", System.getenv("DB_PASS"))
            .addAnnotatedClass(User.class)
            .addAnnotatedClass(Preference.class)
            .buildSessionFactory();

    private static DatabaseUtil instance;

    private DatabaseUtil() {
    }

    public static Session getSession() {
        if (instance == null) {
            instance = new DatabaseUtil();
            instance.initLocales();
        }
        return factory.getCurrentSession();
    }

    public static void changePreference(UserID id, String locale) {
        try (Session session = getSession()) {
            session.beginTransaction();

            Preference pref = session.createSelectionQuery(
                    "SELECT p FROM Preference p WHERE locale = :locale", Preference.class)
                    .setParameter("locale", locale)
                    .getSingleResult();

            User user = (User) session.get(User.class, id);
            user.setPreference(pref.getId());

            session.merge(user);
            session.getTransaction().commit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static String getLocale(UserID id) {
        try (Session session = getSession()) {
            session.beginTransaction();

            User user = (User) session.get(User.class, id);

            Preference pref = session.createSelectionQuery(
                    "SELECT p FROM Preference p WHERE id = :id", Preference.class)
                    .setParameter("id", user.getPreference())
                    .getSingleResult();

            session.getTransaction().commit();

            return pref.getLocale();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static boolean changedNickname(UserID id, String nickName) {
        boolean changed = false;
        try (Session session = getSession()) {
            session.beginTransaction();

            User user = (User) session.get(User.class, id);
            changed = !user.getNickName().equals(nickName);

            session.getTransaction().commit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return changed;
    }

    public static void updateUser(UserID id, String nickName) {
        try (Session session = getSession()) {
            session.beginTransaction();

            User user = (User) session.get(User.class, id);
            user.setNickName(nickName);
            session.merge(user);
            session.getTransaction().commit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void createUser(UserID id, String nickName) {
        try (Session session = getSession()) {
            session.beginTransaction();

            Preference pref = session.createSelectionQuery(
                            "SELECT p FROM Preference p WHERE locale = :locale", Preference.class)
                    .setParameter("locale", LanguageManager.defaultLanguage)
                    .getSingleResult();

            User user = new User(id, nickName, pref.getId());
            session.persist(user);
            session.getTransaction().commit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static User getUser(UserID id) {
        User user = null;
        try (Session session = getSession();){
            session.getTransaction().begin();

            user = session.get(User.class, id);

            session.getTransaction().commit();
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return user;
    }

    private List<Preference> loadPreferencesFromFile(String filePath)  {
        List<Preference> preferences = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int id = 1;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    Preference preference = new Preference();
                    preference.setLocale(parts[0].trim());
                    preference.setId(id++);
                    preferences.add(preference);
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return preferences;
    }

    private void initLocales() {
        try (Session session = getSession();){
            session.getTransaction().begin();

            List<Preference> preferences = loadPreferencesFromFile(LanguageManager.localeTxtPath);
            for(Preference preference : preferences)
                session.merge(preference);

            session.getTransaction().commit();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

}
