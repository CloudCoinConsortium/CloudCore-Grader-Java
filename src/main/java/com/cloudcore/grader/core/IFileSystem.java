package com.cloudcore.grader.core;

import com.cloudcore.grader.utils.FileUtils;
import com.cloudcore.grader.utils.Utils;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public abstract class IFileSystem {

    public enum FileMoveOptions {Replace, Rename}

    public String RootPath;

    public String ImportedFolder;
    public String DetectedFolder;

    public String BankFolder;
    public String FrackedFolder;
    public String CounterfeitFolder;
    public String LostFolder;

    public String LogsFolder;

    public ArrayList<CloudCoin> LoadFolderCoins(String folder) {
        ArrayList<CloudCoin> folderCoins = new ArrayList<>();

        String[] fileNames = FileUtils.selectFileNamesInFolder(folder);
        String extension;
        for (String fileName : fileNames) {
            int index = fileName.lastIndexOf('.');
            if (index > 0) {
                extension = fileName.substring(index + 1);
                fileName = folder + fileName;

                switch (extension) {
                    case "celeb":
                    case "celebrium":
                    case "stack":
                        ArrayList<CloudCoin> coins = FileUtils.loadCloudCoinsFromStack(fileName);
                        if (coins != null)
                            folderCoins.addAll(coins);
                        break;
                }
            }
        }

        return folderCoins;
    }

    public void MoveFile(String SourcePath, String TargetPath, FileMoveOptions options) {
        try {
            if (!Files.exists(Paths.get(TargetPath))) {
                Files.move(Paths.get(SourcePath), Paths.get(TargetPath));
            } else {
                if (options == FileMoveOptions.Replace) {
                    Files.delete(Paths.get(TargetPath));
                    Files.move(Paths.get(SourcePath), Paths.get(TargetPath));
                }
                if (options == FileMoveOptions.Rename) {
                    String targetFileName = SourcePath.substring(SourcePath.lastIndexOf(File.separator) + 1, SourcePath.lastIndexOf('.'));
                    targetFileName += FileUtils.randomString(8).toLowerCase() + ".stack";
                    String targetPath = TargetPath + File.separator + targetFileName;
                    Files.move(Paths.get(SourcePath), Paths.get(targetPath));

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void MoveCoins(ArrayList<CloudCoin> coins, String sourceFolder, String targetFolder) {
        MoveCoins(coins, sourceFolder, targetFolder, ".stack", false);
    }

    public void MoveCoins(ArrayList<CloudCoin> coins, String sourceFolder, String targetFolder, String extension, boolean replaceCoins) {
        ArrayList<CloudCoin> folderCoins = LoadFolderCoins(targetFolder);

        for (CloudCoin coin : coins) {
            String fileName = (coin.FileName());
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
