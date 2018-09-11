package com.cloudcore.grader;

import com.cloudcore.grader.core.CloudCoin;
import com.cloudcore.grader.utils.Utils;
import com.cloudcore.grader.core.FileSystem;
import com.cloudcore.grader.utils.SimpleLogger;

import java.util.ArrayList;

public class Grader {

    
    public SimpleLogger logger;

    /**
     * Categorizes coins into folders based on their pown results.
     */
    public void grade() {
        ArrayList<CloudCoin> detectedCoins = FileSystem.loadFolderCoins(FileSystem.DetectedFolder);

        detectedCoins.forEach(this::GradeSimple); // Apply Grading to all detected coins at once.

        ArrayList<CloudCoin> coinsBank = new ArrayList<>();
        ArrayList<CloudCoin> coinsFracked = new ArrayList<>();
        ArrayList<CloudCoin> coinsCounterfeit = new ArrayList<>();
        ArrayList<CloudCoin> coinsLost = new ArrayList<>();

        for (CloudCoin coin : detectedCoins) {
            if (coin.getFolder().equals(FileSystem.BankFolder)) coinsBank.add(coin);
            else if (coin.getFolder().equals(FileSystem.FrackedFolder)) coinsFracked.add(coin);
            else if (coin.getFolder().equals(FileSystem.CounterfeitFolder)) coinsCounterfeit.add(coin);
            else if (coin.getFolder().equals(FileSystem.LostFolder)) coinsLost.add(coin);
        }

        updateLog("Coin Detection finished.");
        updateLog("Total Passed Coins - " + (coinsBank.size() + coinsFracked.size()) + "");
        updateLog("Total Failed Coins - " + coinsCounterfeit.size() + "");
        updateLog("Total Lost Coins - " + coinsLost.size() + "");

        // Move Coins to their respective folders after sort
        FileSystem.moveCoins(coinsBank, FileSystem.DetectedFolder, FileSystem.BankFolder);
        FileSystem.moveCoins(coinsFracked, FileSystem.DetectedFolder, FileSystem.FrackedFolder);
        FileSystem.moveCoins(coinsCounterfeit, FileSystem.DetectedFolder, FileSystem.CounterfeitFolder);
        FileSystem.moveCoins(coinsLost, FileSystem.DetectedFolder, FileSystem.LostFolder);
    }

    /**
     * Determines the coin's folder based on a simple grading schematic.
     */
    public void GradeSimple(CloudCoin coin) {
        if (isPassingSimple(coin.getPown())) {
            if (isFrackedSimple(coin.getPown()))
                coin.setFolder(FileSystem.FrackedFolder);
            else
                coin.setFolder(FileSystem.BankFolder);
        }
        else {
            if (isHealthySimple(coin.getPown()))
                coin.setFolder(FileSystem.CounterfeitFolder);
            else
                coin.setFolder(FileSystem.LostFolder);
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
