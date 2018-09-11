package com.cloudcore.grader.core;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.File;
import java.util.*;

public class CloudCoin {


    /* JSON Fields */

    @Expose
    @SerializedName("nn")
    private int nn;
    @Expose
    @SerializedName("sn")
    private int sn;
    @Expose
    @SerializedName("an")
    private ArrayList<String> an = new ArrayList<>(Config.nodeCount);
    @Expose
    @SerializedName("ed")
    private String ed;
    @Expose
    @SerializedName("pown")
    private String pown = "uuuuuuuuuuuuuuuuuuuuuuuuu";
    @Expose
    @SerializedName("aoid")
    private ArrayList<String> aoid = new ArrayList<>();


    /* Fields */

    public transient String[] pan = new String[Config.nodeCount];

    public transient String folder;

    public transient String currentFilename;


    /* Methods */

    @Override
    public String toString() {
        return "cloudcoin: (nn:" + getNn() + ", sn:" + getSn() + ", ed:" + getEd() + ", aoid:" + getAoid().toString() + ", an:" + getAn().toString() + ",\n pan:" + Arrays.toString(pan);
    }


    /* Getters and Setters */

    public int getNn() { return nn; }
    public int getSn() { return sn; }
    public ArrayList<String> getAn() { return an; }
    public String getEd() { return ed; }
    public String getPown() { return pown; }
    public ArrayList<String> getAoid() { return aoid; }
    public String getFolder() { return folder; }

    public void setEd(String ed) { this.ed = ed; }
    public void setFolder(String folder) { this.folder = folder; }

    public void setFullFilePath(String fullFilePath) {
        this.folder = fullFilePath.substring(0, 1 + fullFilePath.lastIndexOf(File.separatorChar));
        this.currentFilename = fullFilePath.substring(1 + fullFilePath.lastIndexOf(File.separatorChar, fullFilePath.length()));
    }
}
