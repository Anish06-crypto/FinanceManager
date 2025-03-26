package com.finance.strategy;

import com.finance.domain.Account;

import java.math.BigDecimal;

public class IncomeStrategy implements TransactionStrategy {
    @Override
    public void apply(Account account, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Income amount must be positive");
        }
        BigDecimal newBalance = account.getBalance().add(BigDecimal.valueOf(amount));
        account.setBalance(newBalance);
    }
}
