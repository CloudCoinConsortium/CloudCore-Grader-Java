package com.cloudcore.grader;

import com.cloudcore.grader.core.FileSystem;
import com.cloudcore.grader.desktop.FolderWatcher;
import com.cloudcore.grader.utils.SimpleLogger;

import java.time.Instant;

public class Main {


    public static void main(String[] args) {
        SimpleLogger.writeLog("ServantGraderStarted", "");

        while (true) {
            try {
                FileSystem.createDirectories();

                FolderWatcher watcher = new FolderWatcher(FileSystem.DetectedFolder);
                boolean stop = false;

                if (0 != FileSystem.getTotalCoinsBank(FileSystem.DetectedFolder)[5])
                    Grader.grade();

                while (!stop) {
                    if (watcher.newFileDetected()) {
                        System.out.println(Instant.now().toString() + ": Grading coins...");
                        Grader.grade();
                    }
                }
            } catch (Exception e) {
                System.out.println("Uncaught exception - " + e.getLocalizedMessage());
            }
        }
    }
}
