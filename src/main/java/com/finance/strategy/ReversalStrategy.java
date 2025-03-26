package com.finance.strategy;

import com.finance.domain.Account;

import java.math.BigDecimal;

public class ReversalStrategy implements TransactionStrategy {
    @Override
    public void apply(Account account, double amount) {
        // Just reverse balance change — no validation
        account.setBalance(account.getBalance().add(BigDecimal.valueOf(amount)));
    }
}