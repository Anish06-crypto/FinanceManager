package com.finance.strategy;

import com.finance.domain.Account;

import java.math.BigDecimal;

public class ExpenseStrategy implements TransactionStrategy {
    @Override
    public void apply(Account account, double amount) {
        if (amount >= 0) {
            throw new IllegalArgumentException("Expense amount must be negative");
        }
        BigDecimal newBalance = account.getBalance().add(BigDecimal.valueOf(amount)); // amount is negative
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }
        account.setBalance(newBalance);
    }
}
