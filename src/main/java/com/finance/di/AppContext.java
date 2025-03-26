package com.finance.di;

import com.finance.config.ConfigLoader;
import com.finance.domain.Account;
import com.finance.domain.Transaction;
import com.finance.factory.RepositoryFactory;
import com.finance.notification.ReminderService;
import com.finance.notification.ScheduledReminderService;
import com.finance.repository.Repository;
import com.finance.service.AccountService;
import com.finance.service.AccountServiceImpl;
import com.finance.service.TransactionService;
import com.finance.service.TransactionServiceImpl;

import java.io.IOException;

public class AppContext {

    private static final Repository<Account> accountRepo;
    private static final Repository<Transaction> transactionRepo;
    private static final ReminderService reminderService;

    private static final AccountService accountService;
    private static final TransactionService transactionService;

    static {
        // Load from config
        String acctPath = ConfigLoader.get("account.data.path");
        String txPath = ConfigLoader.get("transaction.data.path");
        int poolSize = ConfigLoader.getInt("scheduler.pool.size");

        // Build repositories
        try {
            accountRepo = RepositoryFactory.createFileBased(acctPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            transactionRepo = RepositoryFactory.createFileBased(txPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Build reminder service
        reminderService = new ScheduledReminderService(poolSize);

        // Build services
        accountService = new AccountServiceImpl(accountRepo, transactionRepo);
        transactionService = new TransactionServiceImpl(transactionRepo, accountRepo, reminderService);
    }

    public static AccountService getAccountService() {
        return accountService;
    }

    public static TransactionService getTransactionService() {
        return transactionService;
    }

    public static ReminderService getReminderService() {
        return reminderService;
    }
}
