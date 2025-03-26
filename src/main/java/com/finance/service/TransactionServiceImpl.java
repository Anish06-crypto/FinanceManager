package com.finance.service;

import com.finance.domain.Account;
import com.finance.domain.Transaction;
import com.finance.notification.ReminderService;
import com.finance.repository.Repository;
import com.finance.strategy.TransactionType;
import com.finance.factory.TransactionStrategyFactory;

import java.time.YearMonth;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.concurrent.atomic.AtomicLong;

import java.time.LocalDate;
import java.util.List;

public class TransactionServiceImpl implements TransactionService {
    private final Repository<Transaction> transactionRepo;
    private final Repository<Account> accountRepo;
    private final ReminderService reminderService;
    private final AtomicLong idCounter = new AtomicLong(1);

    public TransactionServiceImpl(Repository<Transaction> transactionRepo, Repository<Account> accountRepo, ReminderService reminderService) {
        this.transactionRepo = transactionRepo;
        this.accountRepo = accountRepo;
        this.reminderService = reminderService;
    }

    @Override
    public Transaction recordTransaction(Long ignoredId, Long accountId, String desc, double amount) {
        long id = idCounter.getAndIncrement();  // generate ID
        Transaction tx = new Transaction(id, accountId, desc, LocalDate.now(), amount);
        transactionRepo.save(tx);
        Account account = accountRepo.findById(accountId);
        TransactionType type = amount >= 0 ? TransactionType.INCOME : TransactionType.EXPENSE;
        TransactionStrategyFactory.getStrategy(type).apply(account, amount);
        accountRepo.update(account);
        reminderService.schedule(tx);
        return tx;
    }

    @Override
    public List<Transaction> getTransactionsForAccount(Long accountId) {
        return transactionRepo.findAll().stream()
                .filter(tx -> tx.getAccountId().equals(accountId))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteTransaction(Long id) {
        Transaction tx = transactionRepo.findById(id);
        Account account = accountRepo.findById(tx.getAccountId());

        // Reverse the effect of the transaction using raw reversal
        double reversalAmount = -tx.getAmount();
        TransactionStrategyFactory.getReversalStrategy().apply(account, reversalAmount);

        accountRepo.update(account);
        transactionRepo.delete(id);
    }

    @Override
    public double getTotalIncomeForAccount(Long accountId) {
        return getTransactionsForAccount(accountId).stream()
                .filter(tx -> tx.getAmount() > 0)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    @Override
    public double getTotalExpenseForAccount(Long accountId) {
        return getTransactionsForAccount(accountId).stream()
                .filter(tx -> tx.getAmount() < 0)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    @Override
    public double getNetBalanceForAccount(Long accountId) {
        return getTotalIncomeForAccount(accountId) + getTotalExpenseForAccount(accountId);
    }

    @Override
    public Map<YearMonth, List<Transaction>> getMonthlyTransactions(Long accountId) {
        return getTransactionsForAccount(accountId).stream()
                .collect(Collectors.groupingBy(tx -> YearMonth.from(tx.getDate())));
    }

}