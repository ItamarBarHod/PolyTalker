package org.example.main;

import javax.security.auth.login.LoginException;

public class Main {
    public static void main(String[] args) throws Exception {

        try {
            Bot discordBot = new Bot();
        } catch (LoginException e) {
            System.out.println("Error: Provided bot token is invalid!");
        }

    }
}