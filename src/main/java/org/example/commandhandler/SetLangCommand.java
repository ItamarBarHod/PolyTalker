package org.example.commandhandler;

import model.DatabaseUtil;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.example.lib.AudioFileManager;
import org.example.lib.LanguageManager;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class SetLangCommand implements CommandStrategy {
    @Override
    public void execute(SlashCommandInteractionEvent event) {
        OptionMapping option = event.getOption("tts-language");
        if (option == null) {
            return;
        }
        String localeChoice = option.getAsString();
        setLocale(event, localeChoice);
    }

    private void setLocale(@NotNull SlashCommandInteractionEvent event, String language) {
        try {
            LanguageManager manager = new LanguageManager();
            boolean isValidLanguage = manager.isValidLanguage(language);
            if (isValidLanguage) {

                AudioFileManager audioFileManager = new AudioFileManager();
                String nickName = event.getMember().getUser().getEffectiveName();
                String userName = event.getMember().getUser().getName();

                audioFileManager.make(language, nickName, userName);
                DatabaseUtil.changePreference(userName, language);
                event.reply("TTS voice set to: " + language).setEphemeral(true).queue();
            } else {
                event.reply("Invalid language option.").setEphemeral(true).queue();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
