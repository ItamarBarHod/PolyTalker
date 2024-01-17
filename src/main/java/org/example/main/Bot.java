package org.example.main;

import io.github.cdimascio.dotenv.Dotenv;

import model.DatabaseUtil;
import model.UserID;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.example.lib.LanguageManager;
import org.example.listeners.CommandListener;
import org.example.listeners.ChannelListener;
import org.example.listeners.GuildListener;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Bot {

    public Bot() throws LoginException, RuntimeException, InterruptedException, IOException {

        LanguageManager.createLocaleFile();
        DatabaseUtil.getSession();

        JDA jda = initJDA();

        initDatabase(jda);
    }

    private JDA initJDA() throws InterruptedException {
        String token = System.getenv("TOKEN");
        JDA jda = JDABuilder.createDefault(token)
                .setActivity(Activity.playing("/ttshelp"))
                .setStatus(OnlineStatus.ONLINE)
                .build().awaitReady();

        jda.upsertCommand("setlang", "Sets a TTS language")
                .addOption(OptionType.STRING, "tts-language",
                        "Example: /setlang en. For languages, use: /getlang", true)
                .queue();
        jda.upsertCommand("getlang", "Shows the available languages")
                .queue();
        jda.upsertCommand("ttshelp", "Shows available commands")
                .queue();

        jda.addEventListener(new CommandListener());
        jda.addEventListener(new ChannelListener());
        jda.addEventListener(new GuildListener());

        return jda;
    }

    private void initDatabase(JDA jda) {

        jda.getGuilds().forEach(guild -> {
            guild.getVoiceChannels().forEach(voiceChannel -> {
                for (Member member : voiceChannel.getMembers()) {
                    String userName = member.getUser().getName();
                    long guildID = member.getGuild().getIdLong();
                    DatabaseUtil.createUser(new UserID(guildID, userName), "Default");
                }
            });
        });
    }

}
