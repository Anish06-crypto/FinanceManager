package com.finance.factory;

import com.finance.strategy.*;

public class TransactionStrategyFactory {
    public static TransactionStrategy getStrategy(TransactionType type) {
        return switch(type) {
            case INCOME -> new IncomeStrategy();
            case EXPENSE -> new ExpenseStrategy();
        };
    }
    public static TransactionStrategy getReversalStrategy() {
        return new ReversalStrategy();
    }

}