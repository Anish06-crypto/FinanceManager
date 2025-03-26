package com.finance.strategy;

import com.finance.domain.Account;

public interface TransactionStrategy {
    void apply(Account account, double amount);
}
