package com.carta.integrations;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SweetBank {

    public static final String COMMA_DELIMITER = ",";

    public String work(String file) {
        List<String[]> data = this.readCSV(file);
        assert data != null;
        return this.calculateMinMax(data);
    }

    private String calculateMinMax(List<String[]> data) {
        Map<String, Integer> transactionCount = new HashMap<>();
        Map<String, BigInteger> transactionSum = new HashMap<>();
        Map<String, BigDecimal> transactionAvg = new HashMap<>();

        for (String[] account : data) {
            String source = account[0].strip();
            transactionCount.put(source, transactionCount.getOrDefault(source, 0) + 1);
            BigInteger amount = new BigInteger(account[2].strip());
            transactionSum.put(source, transactionSum.getOrDefault(source, BigInteger.ZERO).add(amount));
        }

        for (String source : transactionCount.keySet()) {
            transactionAvg.put(source, getAvg(transactionSum.get(source), transactionCount.get(source)));
        }

        String maxAvgSource = Collections.max(transactionAvg.entrySet(), Map.Entry.comparingByValue()).getKey();
        String formattedOutput = String.format("Account matching criteria is %s because it had %d and %.2f %s " +
                        "transacted."
                , maxAvgSource
                , transactionCount.get(maxAvgSource)
                , getUSD(transactionAvg.get(maxAvgSource))
                , "USD");
        System.out.println(formattedOutput);
        return maxAvgSource;
    }

    private BigDecimal getAvg(BigInteger amount, Integer numTransactions) {
        return new BigDecimal(amount).divide(new BigDecimal(numTransactions), 2, RoundingMode.HALF_UP);
    }

    private double getUSD(BigDecimal amountInCents) {
        return amountInCents.doubleValue() / 100;
    }

    private List<String[]> readCSV(String fileName) {
        List<String[]> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line = "";
            br.readLine(); // Skip headers
            while ((line = br.readLine()) != null) {
                lines.add(line.split(COMMA_DELIMITER));
            }
            return lines;
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
    }
}
