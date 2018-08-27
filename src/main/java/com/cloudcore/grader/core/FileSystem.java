package com.cloudcore.grader.core;

import com.cloudcore.grader.core.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class FileSystem extends IFileSystem {

    public FileSystem(String RootPath) {
        this.RootPath = RootPath;

        ImportedFolder = RootPath + File.separator + Config.TAG_IMPORTED + File.separator;
        DetectedFolder = RootPath + File.separator + Config.TAG_DETECTED + File.separator;

        BankFolder = RootPath + File.separator + Config.TAG_BANK + File.separator;
        FrackedFolder = RootPath + File.separator + Config.TAG_FRACKED + File.separator;
        CounterfeitFolder = RootPath + File.separator + Config.TAG_COUNTERFEIT + File.separator;
        LostFolder = RootPath + File.separator + Config.TAG_LOST + File.separator;

        LogsFolder = RootPath + File.separator + Config.TAG_LOGS + File.separator;
    }

    public boolean CreateDirectories() {
        // Create Subdirectories as per the RootFolder Location
        // Failure will return false

        try {
            Files.createDirectories(Paths.get(RootPath));

            Files.createDirectories(Paths.get(DetectedFolder));

            Files.createDirectories(Paths.get(BankFolder));
            Files.createDirectories(Paths.get(FrackedFolder));
            Files.createDirectories(Paths.get(CounterfeitFolder));
            Files.createDirectories(Paths.get(LostFolder));

            Files.createDirectories(Paths.get(LogsFolder));
        } catch (Exception e) {
            System.out.println("FS#CD: " + e.getLocalizedMessage());
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static File[] GetFilesArray(String path, String[] extensions) {
        final ArrayList<String> extensionsArray = new ArrayList<>(Arrays.asList(extensions));
        File[] files = new File(path).listFiles(pathname -> {
            String filename = pathname.getAbsolutePath();
            String extension = filename.substring(filename.lastIndexOf('.')).toLowerCase();
            return extensionsArray.contains(extension);
        });
        if (files == null)
            files = new File[0];
        return files;
    }
}

