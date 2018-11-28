package com.cloudcore.grader.raida;

import com.cloudcore.grader.core.CloudCoin;
import com.cloudcore.grader.core.FileSystem;
import com.cloudcore.grader.utils.Utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Receipt {


    public String receipt_id;

    public String time;

    public String timezone;

    public String bank_server;

    public int total_authentic;

    public int total_fracked;

    public int total_counterfeit;

    public int total_lost;

    public int total_unchecked;

    public ReceiptDetail[] rd;

    public static void updateReceiptIfItExists(String receipt_id, int a, int b, int c, int d) throws IOException {
        if (!Files.exists(Paths.get(FileSystem.ReceiptsFolder)))
            return;

        String file = new String(Files.readAllBytes(Paths.get(FileSystem.ReceiptsFolder + receipt_id)));
        Receipt receipt = Utils.createGson().fromJson(file, Receipt.class);
        receipt.total_authentic = a;
        receipt.total_fracked = b;
        receipt.total_counterfeit = c;
        receipt.total_lost = d;
        receipt.total_unchecked = 0;
        Files.write(Paths.get(FileSystem.ReceiptsFolder + receipt_id), Utils.createGson().toJson(receipt).getBytes());
    }
}

