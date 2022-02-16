package com.impezar.start;

import com.impezar.invoice.Invoice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main {
    public static void main(String[] args) {

        LocalDateTime l = LocalDateTime.now();
        DateTimeFormatter setFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy \t\t\tHH:mm:ss");
        System.out.println("\t\tABC Bakery\n"+l.format(setFormat));

        Invoice obj = new Invoice();
        obj.startPurchasing();
    }
}
