package org.example.AudioPlayerHandler;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public class AudioPlayerLoadHandler implements AudioLoadResultHandler {

    private final AudioPlayer player;

    public AudioPlayerLoadHandler(AudioPlayer player) {
        this.player = player;
    }

    @Override
    public void trackLoaded(AudioTrack audioTrack) {
        player.playTrack(audioTrack);
    }

    @Override
    public void playlistLoaded(AudioPlaylist audioPlaylist) {
    }

    @Override
    public void noMatches() {
    }

    @Override
    public void loadFailed(FriendlyException e) {
    }
}
