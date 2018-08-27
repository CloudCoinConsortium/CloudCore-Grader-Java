package com.cloudcore.grader.core;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.File;
import java.util.*;

public class CloudCoin {


    /* JSON Fields */

    @Expose
    @SerializedName("nn")
    public int nn;
    @Expose
    @SerializedName("sn")
    private int sn;
    @Expose
    @SerializedName("an")
    public ArrayList<String> an;
    @Expose
    @SerializedName("ed")
    public String ed;
    @Expose
    @SerializedName("pown")
    public String pown;
    @Expose
    @SerializedName("aoid")
    public ArrayList<String> aoid;


    /* Fields */

    public transient String[] pan = new String[Config.nodeCount];

    public transient String folder;

    public transient String currentFilename;


    /* Methods */

    @Override
    public String toString() {
        return "cloudcoin: (nn:" + nn + ", sn:" + sn + ", ed:" + ed + ", aoid:" + aoid.toString() + ", an:" + an.toString() + ",\n pan:" + Arrays.toString(pan);
    }


    /* Getters and Setters */

    public String FileName() {
        return this.getDenomination() + ".CloudCoin." + nn + "." + sn + ".";
    }

    public int getDenomination() {
        int nom;
        if ((sn < 1))
            nom = 0;
        else if ((sn < 2097153))
            nom = 1;
        else if ((sn < 4194305))
            nom = 5;
        else if ((sn < 6291457))
            nom = 25;
        else if ((sn < 14680065))
            nom = 100;
        else if ((sn < 16777217))
            nom = 250;
        else
            nom = 0;

        return nom;
    }

    public int getSn() {
        return sn;
    }

    public void setFullFilePath(String fullFilePath) {
        this.folder = fullFilePath.substring(0, 1 + fullFilePath.lastIndexOf(File.separatorChar));
        this.currentFilename = fullFilePath.substring(1 + fullFilePath.lastIndexOf(File.separatorChar, fullFilePath.length()));
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public String getFolder() {
        return folder;
    }
}
