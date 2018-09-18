package com.cloudcore.grader.core;

import com.cloudcore.grader.utils.CoinUtils;
import com.cloudcore.grader.utils.FileUtils;
import com.cloudcore.grader.utils.Utils;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class FileSystem {


    /* Fields */

    public enum FileMoveOptions {Replace, Rename}

    public static String RootPath = "C:" + File.separator + "CloudCoins-Grader" + File.separator;

    public static String DetectedFolder = RootPath + Config.TAG_DETECTED + File.separator;

    public static String BankFolder = RootPath + Config.TAG_BANK + File.separator;
    public static String FrackedFolder = RootPath + Config.TAG_FRACKED + File.separator;
    public static String CounterfeitFolder = RootPath + Config.TAG_COUNTERFEIT + File.separator;
    public static String LostFolder = RootPath + Config.TAG_LOST + File.separator;

    public static String LogsFolder = RootPath + Config.TAG_LOGS + File.separator;


    /* Methods */

    public static boolean createDirectories() {
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

    /**
     * Loads all CloudCoins from a specific folder.
     *
     * @param folder the folder to search for CloudCoin files.
     * @return an ArrayList of all CloudCoins in the specified folder.
     */
    public static ArrayList<CloudCoin> loadFolderCoins(String folder) {
        ArrayList<CloudCoin> folderCoins = new ArrayList<>();

        String[] filenames = FileUtils.selectFileNamesInFolder(folder);
        for (String filename : filenames) {
            int index = filename.lastIndexOf('.');
            if (index == -1) continue;

            String extension = filename.substring(index + 1);

            switch (extension) {
                case "stack":
                    ArrayList<CloudCoin> coins = FileUtils.loadCloudCoinsFromStack(folder, filename);
                    folderCoins.addAll(coins);
                    break;
            }
        }

        return folderCoins;
    }

    public static void moveCoins(ArrayList<CloudCoin> coins, String sourceFolder, String targetFolder) {
        moveCoins(coins, sourceFolder, targetFolder, ".stack", false);
    }

    public static void moveCoins(ArrayList<CloudCoin> coins, String sourceFolder, String targetFolder, String extension, boolean replaceCoins) {
        ArrayList<CloudCoin> folderCoins = loadFolderCoins(targetFolder);

        for (CloudCoin coin : coins) {
            String fileName = CoinUtils.generateFilename(coin);
            int coinExists = 0;
            for (CloudCoin folderCoin : folderCoins)
                if (folderCoin.getSn() == coin.getSn())
                    coinExists++;
            //int coinExists = (int) Arrays.stream(folderCoins.toArray(new CloudCoin[0])).filter(x -> x.getSn() == coin.getSn()).count();

            if (coinExists > 0 && !replaceCoins) {
                String suffix = FileUtils.randomString(16);
                fileName += suffix.toLowerCase();
            }
            try {
                Gson gson = Utils.createGson();
                Stack stack = new Stack(coin);
                Files.write(Paths.get(targetFolder + fileName + extension), gson.toJson(stack).getBytes(StandardCharsets.UTF_8));
                Files.deleteIfExists(Paths.get(sourceFolder + coin.currentFilename));
            } catch (Exception e) {
                System.out.println(e.getLocalizedMessage());
                e.printStackTrace();
            }
        }
    }
}

