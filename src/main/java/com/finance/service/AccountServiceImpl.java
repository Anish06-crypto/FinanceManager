package com.finance.service;
import com.finance.domain.Account;
import com.finance.domain.Transaction;
import com.finance.repository.Repository;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class AccountServiceImpl implements AccountService {
    private final Repository<Account> repo;
    private final Repository<Transaction> transactionRepo;
    private final AtomicLong idCounter = new AtomicLong(1);

    public AccountServiceImpl(Repository<Account> repo, Repository<Transaction> transactionRepo) { this.repo = repo; this.transactionRepo = transactionRepo; }

    @Override
    public Account createAccount(String name) {
        long id = idCounter.getAndIncrement();        // AtomicLong or similar
        Account account = new Account(id, name);
        repo.save(account);
        return account;
    }

    @Override
    public void deposit(Long accountId, BigDecimal amount) {
        Account acct = repo.findById(accountId);
        acct.deposit(amount);
        repo.update(acct);
    }

    @Override
    public void withdraw(Long accountId, BigDecimal amount) {
        Account acct = repo.findById(accountId);
        acct.withdraw(amount);
        repo.update(acct);
    }

    @Override
    public List<Account> getAllAccounts() {
        return repo.findAll();
    }

    @Override
    public void deleteAccount(Long id) {
        Account account = repo.findById(id);

        boolean hasTransactions = transactionRepo.findAll().stream()
                .anyMatch(tx -> tx.getAccountId().equals(id));

        if (hasTransactions) {
            throw new IllegalStateException("Cannot delete account with existing transactions.");
        }

        repo.delete(id);
    }
}