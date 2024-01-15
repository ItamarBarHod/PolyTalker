package org.example.commandhandler;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

public class HelpCommand implements CommandStrategy {
    @Override
    public void execute(@NotNull SlashCommandInteractionEvent event) {
        String reply = "/setlang - set the TTS language\n" +
                "/getlang - get the available languages";
        event.reply(reply).setEphemeral(true).queue();
    }
}