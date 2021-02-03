package com.carta.integrations;

import java.math.BigInteger;
import java.util.Map;

public class TransactionAggregate {
    Map<String, BigInteger> transactionSum;
    Map<String, Integer> transactionCount;

    public TransactionAggregate(Map<String, BigInteger> transactionSum, Map<String, Integer> transactionCount) {
        this.transactionSum = transactionSum;
        this.transactionCount = transactionCount;
    }

    public Map<String, BigInteger> getTransactionSum() {
        return transactionSum;
    }

    public Map<String, Integer> getTransactionCount() {
        return transactionCount;
    }
}
