package com.cloudcore.grader;

import com.cloudcore.grader.core.CloudCoin;
import com.cloudcore.grader.utils.Utils;
import com.cloudcore.grader.core.FileSystem;
import com.cloudcore.grader.utils.SimpleLogger;

import java.util.ArrayList;

public class Grader {

    public static FileSystem FS;
    public SimpleLogger logger;

    public Grader(FileSystem fileSystem) {
        FS = fileSystem;
    }

    /**
     * Categorizes coins into folders based on their pown results.
     */
    public void grade() {
        ArrayList<CloudCoin> detectedCoins = FS.LoadFolderCoins(FS.DetectedFolder);

        detectedCoins.forEach(this::GradeSimple); // Apply Grading to all detected coins at once.

        ArrayList<CloudCoin> coinsBank = new ArrayList<>();
        ArrayList<CloudCoin> coinsFracked = new ArrayList<>();
        ArrayList<CloudCoin> coinsCounterfeit = new ArrayList<>();
        ArrayList<CloudCoin> coinsLost = new ArrayList<>();

        for (CloudCoin coin : detectedCoins) {
            if (coin.getFolder().equals(FS.BankFolder)) coinsBank.add(coin);
            else if (coin.getFolder().equals(FS.FrackedFolder)) coinsFracked.add(coin);
            else if (coin.getFolder().equals(FS.CounterfeitFolder)) coinsCounterfeit.add(coin);
            else if (coin.getFolder().equals(FS.LostFolder)) coinsLost.add(coin);
        }

        updateLog("Coin Detection finished.");
        updateLog("Total Passed Coins - " + (coinsBank.size() + coinsFracked.size()) + "");
        updateLog("Total Failed Coins - " + coinsCounterfeit.size() + "");
        updateLog("Total Lost Coins - " + coinsLost.size() + "");

        // Move Coins to their respective folders after sort
        FS.MoveCoins(coinsBank, FS.DetectedFolder, FS.BankFolder);
        FS.MoveCoins(coinsFracked, FS.DetectedFolder, FS.FrackedFolder);
        FS.MoveCoins(coinsCounterfeit, FS.DetectedFolder, FS.CounterfeitFolder);
        FS.MoveCoins(coinsLost, FS.DetectedFolder, FS.LostFolder);
    }

    /**
     * Determines the coin's folder based on a simple grading schematic.
     */
    public void GradeSimple(CloudCoin coin) {
        if (isPassingSimple(coin.getPown())) {
            if (isFrackedSimple(coin.getPown()))
                coin.setFolder(FS.FrackedFolder);
            else
                coin.setFolder(FS.BankFolder);
        }
        else {
            if (isHealthySimple(coin.getPown()))
                coin.setFolder(FS.CounterfeitFolder);
            else
                coin.setFolder(FS.LostFolder);
        }
    }

    /**
     * Checks to see if the pown result is a passing grade.
     *
     * @return true if the pown result contains more than 20 passing grades.
     */
    public boolean isPassingSimple(String pown) {
        return (Utils.charCount(pown, 'p') >= 20);
    }

    /**
     * Checks to see if the pown result is fracked.
     *
     * @return true if the pown result contains more than 5 fracked grades.
     */
    public boolean isFrackedSimple(String pown) {
        return (pown.indexOf('f') != -1);
    }

    /**
     * Checks to see if the pown result is in good health. Unhealthy grades are errors and no-responses.
     *
     * @return true if the pown result contains more than 20 passing or failing grades.
     */
    public boolean isHealthySimple(String pown) {
        return (Utils.charCount(pown, 'p') + Utils.charCount(pown, 'f') >= 20);
    }

    public void updateLog(String message) {
        System.out.println(message);
        logger.Info(message);
    }
}
