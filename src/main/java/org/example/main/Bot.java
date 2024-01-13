package org.example.main;

import io.github.cdimascio.dotenv.Dotenv;

import model.DatabaseUtil;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.example.lib.LanguageManager;
import org.example.listeners.CommandListener;
import org.example.listeners.EventListener;

import javax.security.auth.login.LoginException;
import java.io.IOException;

public class Bot {

    private final Dotenv config;

    public Bot() throws LoginException, RuntimeException, InterruptedException, IOException {

        config = Dotenv.configure().load();
        initJDA(config);

        LanguageManager manager = new LanguageManager();
        manager.createLocaleFile();

        DatabaseUtil.getSession();

    }

    private void initJDA(Dotenv config) throws InterruptedException {
        String token = config.get("TOKEN");
        JDA jda = JDABuilder.createDefault(token)
                .setActivity(Activity.watching("Bar Ronen"))
                .setStatus(OnlineStatus.ONLINE)
                .build().awaitReady();

        jda.upsertCommand("setlang" , "Sets a TTS language")
                .addOption(OptionType.STRING , "tts-language" , "please provide a language", true)
                .queue();
        jda.upsertCommand("getlang" , "Shows the available languages")
                .queue();
        jda.upsertCommand("ttshelp" , "Shows available commands")
                .queue();

        jda.addEventListener(new CommandListener());
        jda.addEventListener(new EventListener());
    }

    public Dotenv getConfig() {
        return config;
    }

}
