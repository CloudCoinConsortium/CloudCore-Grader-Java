package com.cloudcore.grader;

import com.cloudcore.grader.core.FileSystem;
import com.cloudcore.grader.desktop.FolderWatcher;
import com.cloudcore.grader.utils.SimpleLogger;

import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {


    public static void main(String[] args) {
        SimpleLogger.writeLog("ServantStarted", "");
        singleRun = isSingleRun(args);
        if (args.length != 0 && Files.exists(Paths.get(args[0]))) {
            System.out.println("New root path: " + args[0]);
            FileSystem.changeRootPath(args[0]);
        }
        FileSystem.createDirectories();

        FolderWatcher watcher = new FolderWatcher(FileSystem.DetectedFolder);

        if (0 != FileSystem.getTotalCoins(FileSystem.DetectedFolder)[5]) {
            Grader.grade();
            exitIfSingleRun();
        }

        while (true) {
            try {
                Thread.sleep(1000);

                if (watcher.newFileDetected()) {
                    Grader.grade();
                    exitIfSingleRun();
                }
            } catch (Exception e) {
                System.out.println("Uncaught exception - " + e.getLocalizedMessage());
            }
        }
    }

    public static boolean singleRun = false;
    public static boolean isSingleRun(String[] args) {
        for (String arg : args)
            if (arg.equals("singleRun"))
                return true;
        return false;
    }
    public static void exitIfSingleRun() {
        if (singleRun)
            System.exit(0);
    }
}
