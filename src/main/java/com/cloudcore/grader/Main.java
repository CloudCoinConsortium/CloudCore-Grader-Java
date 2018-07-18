package com.cloudcore.grader;

import com.cloudcore.grader.core.FileSystem;
import com.cloudcore.grader.utils.SimpleLogger;

import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main {

    public static String rootFolder = Paths.get("C:/CloudCoins-Grader").toAbsolutePath().toString();

    public static FileSystem FS;
    public static SimpleLogger logger;

    public static void main(String[] args) {
        try {
            setup();

            Grader grader = new Grader(FS);
            grader.logger = logger;

            System.out.println("Grading coins...");
            grader.grade();
        } catch (Exception e) {
            System.out.println("Uncaught exception - " + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    private static void setup() {
        FS = new FileSystem(rootFolder);
        FS.CreateDirectories();

        logger = new SimpleLogger(FS.LogsFolder + "logs" +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")).toLowerCase() + ".log", true);
    }
}
