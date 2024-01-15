package org.example.listeners;

import model.DatabaseUtil;
import model.UserID;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class GuildListener extends ListenerAdapter {
    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        Guild guild = event.getGuild();
        System.out.println("Bot joined guild: " + guild.getName());

        guild.getVoiceChannels().forEach(voiceChannel -> {
            for (Member member : voiceChannel.getMembers()) {
                String userName = member.getUser().getName();
                UserID id = new UserID(guild.getIdLong(), userName);
                DatabaseUtil.createUser(id, "Default");
            }
        });
    }
}
