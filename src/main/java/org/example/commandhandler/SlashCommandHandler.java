package org.example.commandhandler;

import java.util.HashMap;
import java.util.Map;

public class SlashCommandHandler {
    private final Map<String, CommandStrategy> commandStrategies;

    public SlashCommandHandler() {
        commandStrategies = new HashMap<>();
        commandStrategies.put("setlang", new SetLangCommand());
        commandStrategies.put("getlang", new GetLangCommand());
        commandStrategies.put("ttshelp", new HelpCommand());
    }

    public Map<String, CommandStrategy> getCommandStrategies() {
        return commandStrategies;
    }
}