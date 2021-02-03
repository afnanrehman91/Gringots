package com.carta.integrations;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

public class AcmeFinancials {

    public String process(String file) {
        TransactionAggregate transactionAggregate = populateTransactions(file);
        assert transactionAggregate != null;
        return findAccountWithMaxAvgTransaction(transactionAggregate);
    }

    private String findAccountWithMaxAvgTransaction(TransactionAggregate transactionAggregate) {
        BigDecimal maxAvgAmount = BigDecimal.ZERO;
        String maxSourceAccount = null;
        for (String sourceAccount : transactionAggregate.getTransactionSum().keySet()) {
            BigDecimal avgAmount = new BigDecimal(transactionAggregate.getTransactionSum().get(sourceAccount))
                    .divide(BigDecimal.valueOf(transactionAggregate.getTransactionCount()
                            .get(sourceAccount)), 2, RoundingMode.HALF_DOWN);
            if (maxAvgAmount.compareTo(BigDecimal.ZERO) == 0 || avgAmount.compareTo(maxAvgAmount) > 0) {
                maxAvgAmount = avgAmount;
                maxSourceAccount = sourceAccount;
            }
        }
        return maxSourceAccount;
    }

    private TransactionAggregate populateTransactions(String file) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, BigInteger> transactionSum = new HashMap<>();
        Map<String, Integer> transactionCount = new HashMap<>();
        try {
            JsonNode json = objectMapper.readTree(new File(file));
            JsonNode batches = json.get("batches");
            for (final JsonNode batch : batches) {
                JsonNode transactions = batch.get("transactions");
                for (final JsonNode transaction : transactions) {
                    String source = transaction.get("source_account").asText();
                    transactionCount.put(source, transactionCount.getOrDefault(source, 0) + 1);
                    BigInteger previousSum = transactionSum.getOrDefault(source, BigInteger.ZERO);
                    String amount = transaction.get("amount_in_cents").asText();
                    BigInteger totalSum = previousSum.add(new BigInteger(amount));
                    transactionSum.put(source, totalSum);
                }
            }
            return new TransactionAggregate(transactionSum, transactionCount);
        } catch (IOException e) {
            return null;
        }
    }
}
