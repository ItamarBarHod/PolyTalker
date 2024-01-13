package org.example.listeners;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import model.DatabaseUtil;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import org.example.AudioPlayerHandler.AudioPlayerLoadHandler;
import org.example.AudioPlayerHandler.AudioPlayerSendHandler;
import org.example.lib.AudioFileManager;
import org.example.lib.LanguageManager;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public class EventListener extends ListenerAdapter {
    private final AudioPlayerManager playerManager;

    public EventListener() {
        this.playerManager = initAudioPlayerManager();
    }

    private AudioPlayerManager initAudioPlayerManager() {
        AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);
        return playerManager;
    }

    @Override
    public void onGuildVoiceUpdate(@NotNull GuildVoiceUpdateEvent event) {
        super.onGuildVoiceUpdate(event);

        VoiceChannel channel = (VoiceChannel) event.getChannelJoined();
        if (event.getMember().getUser().isBot() || channel == null) {
            return; // Skip the action
        }

        String userName = event.getMember().getUser().getName();
        String nickName = event.getMember().getEffectiveName();

        if (!DatabaseUtil.userExists(userName)) {
            createUser(userName, nickName, null);
        } else if (DatabaseUtil.changedNickname(userName, nickName)) {
            updateUser(userName, nickName, DatabaseUtil.getLocale(userName));
        }

        playAudio(event, channel);
    }

    private void playAudio(GuildVoiceUpdateEvent event, VoiceChannel channel) {
        final String userName = event.getMember().getUser().getName();
        final String audioPath = "./sounds/" + userName +".mp3";
        AudioManager audioManager = event.getGuild().getAudioManager();

        if (!audioManager.isConnected()) {
            audioManager.openAudioConnection(channel); // connect
            loadAndPlay(audioPath, audioManager);
        }
    }

    private void updateUser(String userName, String nickName, String language) {
        createFile(userName, nickName, language); // default language
        DatabaseUtil.updateUser(userName, nickName);
    }

    private void createUser(String userName, String nickName, Object o) {
        createFile(userName, nickName, null);
        DatabaseUtil.createUser(userName, nickName);
    }

    private void createFile(String userName, String nickName, String language) {
        AudioFileManager fileMaker = new AudioFileManager();
        String setLang = language == null ? LanguageManager.defaultLanguage : language;

        try {
            fileMaker.delete(userName);
            fileMaker.make(setLang, nickName, userName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        File f = new File("./sounds/" + userName + ".mp3");
        while (!f.exists()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void loadAndPlay(String filePath, AudioManager audioManager) {
        AudioPlayer player = playerManager.createPlayer();
        audioManager.setSendingHandler(new AudioPlayerSendHandler(player));

        AudioPlayerLoadHandler handler = new AudioPlayerLoadHandler(player);
        playerManager.loadItem(filePath, handler);

        player.addListener(new AudioEventAdapter() {
            @Override
            public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
                if (endReason.mayStartNext) {
                    audioManager.closeAudioConnection();
                }
            }
        });
    }
}