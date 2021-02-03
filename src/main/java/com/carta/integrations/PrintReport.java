package com.carta.integrations;

import java.util.List;

public class PrintReport {
    public static void main(String[] args) {
        if (args.length == 0)
            return;

        String nameOfBank = args[0];
        String resourcesPath = "./src/main/resources/";
        if ("sweet_bank".equals(nameOfBank)) {
            List<String> fileNames = List.of("sweet_bank_financials_1.csv",
                    "sweet_bank_financials_2.csv",
                    "sweet_bank_financials_3.csv");
            for (String fileName : fileNames) {
                System.out.println(fileName);
                System.out.println(new SweetBank().work(resourcesPath + fileName));
            }
        }

        if ("acme_bank".equals(nameOfBank)) {
            String fileName = "acme_bank_financials_1.json";
            System.out.println(fileName);
            System.out.println(new AcmeFinancials().process(resourcesPath + fileName));
        }
    }
}
