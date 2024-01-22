package org.example.listeners;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import model.DatabaseUtil;
import model.User;
import model.UserID;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import org.example.AudioPlayerHandler.AudioPlayerLoadHandler;
import org.example.AudioPlayerHandler.AudioPlayerSendHandler;
import org.example.lib.AudioFileManager;
import org.example.lib.FileAndConsoleLogger;
import org.example.lib.LanguageManager;
import org.jetbrains.annotations.NotNull;


import static org.example.lib.AudioFileManager.createAudioPath;

public class ChannelListener extends ListenerAdapter {
    private final AudioPlayerManager playerManager;

    public ChannelListener() {
        this.playerManager = initializeAudioPlayerManager();
    }

    @Override
    public void onGuildVoiceUpdate(@NotNull GuildVoiceUpdateEvent event) {
        super.onGuildVoiceUpdate(event);

        Member selfMember = event.getGuild().getSelfMember();
        boolean canConnect = selfMember.hasPermission(Permission.VOICE_CONNECT);
        if(!canConnect) {
            FileAndConsoleLogger.logWarning("Cannot join to channel because of voice connected permission");
            return;
        }

        String userName = null;
        String nickName = null;
        long guildID = -1;
        User user = null;
        FileAndConsoleLogger.logInfo("Event:" + event);
        try {
            boolean isBot = event.getMember().getUser().isBot();
            VoiceChannel channel = (VoiceChannel) event.getChannelJoined();
            if (isBot || channel == null || channel.getName().equals("AFK")) {
                return;
            }
            userName = event.getMember().getUser().getName();
            nickName = event.getMember().getEffectiveName();
            guildID = event.getGuild().getIdLong();
            UserID id = new UserID(guildID, userName);
            user = DatabaseUtil.getUser(id);

            if (user == null) {
                createUser(id, nickName);
            } else if (DatabaseUtil.changedNickname(id, nickName)) {
                updateUser(id, nickName, DatabaseUtil.getLocale(id));
            }

            playAudio(event, channel);
        } catch (Exception e) {
            FileAndConsoleLogger.logError(e.getMessage());
            FileAndConsoleLogger.logInfo("Username: " + userName + " nickName: " + nickName + " guildID: " + guildID);
            FileAndConsoleLogger.logInfo(event.getGuild() + " " + event.getMember());
            FileAndConsoleLogger.logInfo("User: " + user);
        }
    }

    private void playAudio(GuildVoiceUpdateEvent event, VoiceChannel channel) {
        String userName = event.getMember().getUser().getName();
        long guildID = event.getGuild().getIdLong();
        UserID id = new UserID(guildID, userName);

        AudioManager audioManager = event.getGuild().getAudioManager();

        if (!audioManager.isConnected()) {
            audioManager.openAudioConnection(channel); // connect
            loadAndPlay(id, audioManager);
        }
    }

    private void updateUser(UserID id, String nickName, String language) {
        createFile(id, nickName, language);
        DatabaseUtil.updateUser(id, nickName);
    }

    private void createUser(UserID id, String nickName) {
        createFile(id, nickName, null); //default language
        DatabaseUtil.createUser(id, nickName);
    }

    private void createFile(UserID id, String nickName, String language) {
        String setLang = language == null ? LanguageManager.defaultLanguage : language;

        AudioFileManager.delete(id);
        AudioFileManager.make(setLang, nickName, id);
    }

    private void loadAndPlay(UserID id, AudioManager audioManager) {
        AudioPlayer player = playerManager.createPlayer();
        audioManager.setSendingHandler(new AudioPlayerSendHandler(player));

        AudioPlayerLoadHandler handler = new AudioPlayerLoadHandler(player);
        String path = createAudioPath(id);
        System.out.println("Trying to play: " + path);

        playerManager.loadItem(path, handler);

        player.addListener(new AudioEventAdapter() {
            @Override
            public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
                if (endReason.mayStartNext) {
                    audioManager.closeAudioConnection();
                }
            }
        });
    }

    private AudioPlayerManager initializeAudioPlayerManager() {
        AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);
        return playerManager;
    }
}