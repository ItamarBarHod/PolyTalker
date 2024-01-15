package org.example.commandhandler;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.example.lib.LanguageManager;
import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;

public class GetLangCommand implements CommandStrategy {
    @Override
    public void execute(@NotNull SlashCommandInteractionEvent event)  {
        String languages = null;
        try {
            languages = LanguageManager.getLanguages();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        final String finalLanguages = languages;
            event.getMember().getUser().openPrivateChannel()
                    .queue(privateChannel -> {
                        privateChannel.sendMessage(finalLanguages).queue();
                    });
            event.reply("Sent you a private message").setEphemeral(true).queue();
    }
}