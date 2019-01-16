package com.cloudcore.grader;

import com.cloudcore.grader.core.FileSystem;
import com.cloudcore.grader.desktop.FolderWatcher;
import com.cloudcore.grader.utils.SimpleLogger;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;

public class Main {


    public static void main(String[] args) {
        SimpleLogger.writeLog("ServantStarted", "");

        while (true) {
            try {
                if (args.length != 0 && Files.exists(Paths.get(args[0]))) {
                    System.out.println("New root path: " + args[0]);
                    FileSystem.changeRootPath(args[0]);
                }
                FileSystem.createDirectories();

                FolderWatcher watcher = new FolderWatcher(FileSystem.DetectedFolder);
                boolean stop = false;

                if (0 != FileSystem.getTotalCoins(FileSystem.DetectedFolder)[5])
                    Grader.grade();

                while (!stop) {
                    if (watcher.newFileDetected())
                        Grader.grade();
                }
            } catch (Exception e) {
                System.out.println("Uncaught exception - " + e.getLocalizedMessage());
            }
        }
    }
}
