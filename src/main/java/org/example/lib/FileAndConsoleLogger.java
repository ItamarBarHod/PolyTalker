package org.example.lib;

import java.io.IOException;
import java.util.logging.*;

public class FileAndConsoleLogger {
    private static final Logger logger = Logger.getLogger(FileAndConsoleLogger.class.getName());

    static {
        configureLogger();
    }

    private static void configureLogger() {
        logger.setLevel(Level.ALL);

        try {
            FileHandler fileHandler = new FileHandler("/logs/logfile.log", true);
            fileHandler.setLevel(Level.ALL);

            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setLevel(Level.ALL);

            logger.addHandler(fileHandler);
            logger.addHandler(consoleHandler);

            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
            consoleHandler.setFormatter(formatter);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void logInfo(String message) {
        logger.info(message);
    }

    public static void logWarning(String message) {
        logger.warning(message);
    }

    public static void logError(String message) {
        logger.severe(message);
    }
}