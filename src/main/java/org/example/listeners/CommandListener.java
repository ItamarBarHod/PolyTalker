package org.example.listeners;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import org.example.commandhandler.CommandStrategy;
import org.example.commandhandler.SlashCommandHandler;
import org.jetbrains.annotations.NotNull;

public class CommandListener extends ListenerAdapter {

    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        super.onSlashCommandInteraction(event);

        String command = event.getName();
        SlashCommandHandler handler = new SlashCommandHandler();
        CommandStrategy strategy = handler.getCommandStrategies().get(command);

        if (strategy != null) {
            strategy.execute(event);
        }
    }
}
