package org.example.lib;

import model.User;
import model.UserID;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class AudioFileManager {

    public static final String soundPath = "./sounds/";
    public static void make(String language, String nickName, UserID id) {

        String[] command = getMakeCommand(language, nickName, id);
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(Arrays.asList(command));
            processBuilder.directory(new File(soundPath));
            Process process = processBuilder.start();
            process.waitFor();
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String[] getMakeCommand(String language, String nickName, UserID id) {
        return new String[]{"gtts-cli", "-l", language, "\""+nickName+"\"", "--output", id.toString() + ".mp3"};
    }

    public static void delete(UserID id) {

        try {
            Path path = Paths.get(createAudioPath(id));
            Files.deleteIfExists(path);
        } catch (IOException ex) {
            System.out.println("File error");
        }
    }

    public static String createAudioPath(UserID id) {
        return soundPath + id.toString() + ".mp3";
    }
}