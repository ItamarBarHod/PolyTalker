package org.example.commandhandler;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public interface CommandStrategy {
    void execute(SlashCommandInteractionEvent event);
}