package org.example.listeners;

import model.DatabaseUtil;
import model.User;
import model.UserID;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.example.commandhandler.CommandStrategy;
import org.example.commandhandler.SlashCommandHandler;
import org.example.lib.FileAndConsoleLogger;
import org.jetbrains.annotations.NotNull;

public class CommandListener extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        super.onSlashCommandInteraction(event);

        String userName = null;
        User user = null;
        try {
            userName = event.getMember().getUser().getName();
            long guildID = event.getGuild().getIdLong();
            user = DatabaseUtil.getUser(new UserID(guildID, userName));
            if (user == null) {
                event.reply("New user - please enter a channel first").setEphemeral(true).queue();
                return;
            }

            String command = event.getName();
            SlashCommandHandler handler = new SlashCommandHandler();
            CommandStrategy strategy = handler.getCommandStrategies().get(command);

            if (strategy != null) {
                strategy.execute(event);
            }
        } catch (Exception e) {
            FileAndConsoleLogger.logError(e.getMessage());
            FileAndConsoleLogger.logInfo("Username: " + userName + " member: " + event.getMember() + " guildID: " + event.getGuild());
            FileAndConsoleLogger.logInfo("User: " + user);
        }
    }
}
