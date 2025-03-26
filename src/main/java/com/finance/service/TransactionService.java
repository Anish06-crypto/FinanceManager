package com.finance.service;

import com.finance.domain.Transaction;
import java.time.YearMonth;

import java.util.List;
import java.util.Map;

public interface TransactionService {
    Transaction recordTransaction(Long id, Long accountId, String desc, double amount);
    List<Transaction> getTransactionsForAccount(Long accountId);
    void deleteTransaction(Long id);
    double getTotalIncomeForAccount(Long accountId);
    double getTotalExpenseForAccount(Long accountId);
    double getNetBalanceForAccount(Long accountId);
    Map<YearMonth, List<Transaction>> getMonthlyTransactions(Long accountId);

}