package org.example.lib;

import org.w3c.dom.ls.LSOutput;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class AudioFileManager {

    public static final String soundPath = "./sounds/";
    public void make(String language, String nickName, String userName) throws IOException {

        String[] command = getMakeCommand(language, nickName, userName);

        ProcessBuilder processBuilder = new ProcessBuilder(Arrays.asList(command));
        processBuilder.directory(new File(soundPath));
        Process process = processBuilder.start();
    }

    private String[] getMakeCommand(String language, String nickName, String userName) {
        return new String[]{"gtts-cli", "-l", language, "\""+nickName+"\"", "--output", userName + ".mp3"};
    }

    public void delete(String userName) {

        try {
            Path path = Paths.get(soundPath + userName + ".mp3");
            Files.deleteIfExists(path);

        } catch (IOException ex) {
            System.out.println("File error");
        }
    }
}