package org.example.lib;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LanguageManager {

    public final static String defaultLanguage = "ja";
    public final static String localeTxtPath = "./locale/locale.txt";

    public boolean isValidLanguage(String language) throws FileNotFoundException {

        File file = new File(localeTxtPath);
        Scanner scanner = new Scanner(file);

        while (scanner.hasNextLine()) {
            String result = findSubstring(scanner.nextLine());
            if (result.equals(language)) {
                return true;
            }
        }
        return false;
    }

    private static String findSubstring(String line) {

        int startIndex = line.indexOf(' ');
        int endIndex = line.indexOf(':', startIndex);

        if (startIndex != -1 && endIndex != -1) {
            return line.substring(startIndex + 1, endIndex).trim();
        }
        return "";
    }

    public static void createLocaleFile() throws IOException {

        ProcessBuilder processBuilder = new ProcessBuilder("gtts-cli", "--all");
        String localePath = "./locale/";
        processBuilder.directory(new File(localePath));
        processBuilder.redirectOutput(new File(localeTxtPath));
        Process process = processBuilder.start();
    }

    public static String getLanguages() throws FileNotFoundException {
        File file = new File(localeTxtPath);
        Scanner scanner = new Scanner(file);
        StringBuilder languages = new StringBuilder();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            int index = line.indexOf(String.valueOf(":"));
            String fixedLine = line.replace(":", " -> ");
            languages.append(fixedLine).append('\n');
        }
        return languages.toString();
    }
}

