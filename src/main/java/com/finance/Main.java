package com.finance;

import com.finance.config.ConfigLoader;
import com.finance.di.AppContext;
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
import com.finance.util.CsvUtil;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        AccountService accountService = AppContext.getAccountService();
        TransactionService txService = AppContext.getTransactionService();
        ReminderService reminderService = AppContext.getReminderService();

        while (true) {
            System.out.println("""
                                1) Create Account
                                2) Record Transaction
                                3) List Transactions
                                4) Delete Transaction
                                5) Delete Account
                                6) View Summary
                                7) Monthly Report
                                8) Export Transactions
                                9) Import Transactions
                                10) Exit
                                """);
            System.out.print("Select an option: ");
            switch (scanner.nextLine().trim()) {
                case "1" -> createAccount(accountService);
                case "2" -> recordTransaction(txService);
                case "3" -> listTransactions(txService);
                case "4" -> deleteTransaction(txService);
                case "5" -> deleteAccount(accountService);
                case "6" -> viewSummary(txService);
                case "7" -> viewMonthlyReport(txService);
                case "8" -> exportTransactions(txService);
                case "9" -> importTransactions(txService);
                case "10" -> {
                    reminderService.shutdown();
                    System.out.println("Goodbye!");
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice, try again.");
            }
        }
    }

    private static void createAccount(AccountService service) {
        System.out.print("Enter account name: ");
        String name = scanner.nextLine();
        Account account = service.createAccount(name);
        System.out.println("Created: " + account);
    }

    private static void recordTransaction(TransactionService service) {
        try {
            System.out.print("Account ID: ");
            long acctId = Long.parseLong(scanner.nextLine());
            System.out.print("Description: ");
            String desc = scanner.nextLine();
            System.out.print("Amount (+ income / - expense): ");
            double amount = Double.parseDouble(scanner.nextLine());
            Transaction tx = service.recordTransaction(null, acctId, desc, amount);
            System.out.println("Recorded: " + tx);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void listTransactions(TransactionService service) {
        System.out.print("Account ID: ");
        long acctId = Long.parseLong(scanner.nextLine());
        service.getTransactionsForAccount(acctId).forEach(System.out::println);
    }

    private static void deleteTransaction(TransactionService service) {
        try {
            System.out.print("Enter Account ID: ");
            long accountId = Long.parseLong(scanner.nextLine());

            List<Transaction> transactions = service.getTransactionsForAccount(accountId);
            if (transactions.isEmpty()) {
                System.out.println("⚠️ No transactions found for this account.");
                return;
            }

            System.out.println("🔎 Transactions:");
            transactions.forEach(tx -> System.out.println(tx));

            System.out.print("Enter Transaction ID to delete: ");
            long txId = Long.parseLong(scanner.nextLine());

            // Confirm deletion
            System.out.print("Type DELETE to confirm: ");
            if (!scanner.nextLine().trim().equalsIgnoreCase("DELETE")) {
                System.out.println("❌ Cancelled.");
                return;
            }

            service.deleteTransaction(txId);
            System.out.println("✅ Transaction deleted.");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


    private static void deleteAccount(AccountService service) {
        try {
            System.out.print("Enter Account ID to delete: ");
            long id = Long.parseLong(scanner.nextLine());

            System.out.print("Are you sure? Type YES to confirm: ");
            if (!scanner.nextLine().trim().equalsIgnoreCase("YES")) {
                System.out.println("❌ Cancelled.");
                return;
            }

            service.deleteAccount(id);
            System.out.println("✅ Account deleted.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void viewSummary(TransactionService service) {
        try {
            System.out.print("Enter Account ID: ");
            long accountId = Long.parseLong(scanner.nextLine());

            double income = service.getTotalIncomeForAccount(accountId);
            double expense = service.getTotalExpenseForAccount(accountId);
            double net = service.getNetBalanceForAccount(accountId);

            System.out.printf("""
            Summary for Account %d:
            Total Income:  %.2f
            Total Expense: %.2f
            Net Balance:   %.2f%n
            """, accountId, income, expense, net);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void viewMonthlyReport(TransactionService service) {
        try {
            System.out.print("Enter Account ID: ");
            long accountId = Long.parseLong(scanner.nextLine());

            Map<YearMonth, List<Transaction>> grouped = service.getMonthlyTransactions(accountId);

            grouped.forEach((month, txs) -> {
                System.out.println("📅 " + month);
                txs.forEach(System.out::println);
                System.out.println("----------------------");
            });
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void exportTransactions(TransactionService service) {
        try {
            System.out.print("Enter Account ID: ");
            long acctId = Long.parseLong(scanner.nextLine());

            List<Transaction> txs = service.getTransactionsForAccount(acctId);
            System.out.print("Enter output file path (e.g., exports/account2.csv): ");
            String filePath = scanner.nextLine();

            CsvUtil.exportTransactionsToCsv(txs, filePath);
            System.out.println("✅ Export complete.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void importTransactions(TransactionService service) {
        try {
            System.out.print("Enter CSV file path to import: ");
            String filePath = scanner.nextLine();

            List<Transaction> txs = CsvUtil.importTransactionsFromCsv(filePath);

            for (Transaction tx : txs) {
                service.recordTransaction(tx.getId(), tx.getAccountId(), tx.getDescription(), tx.getAmount());
            }

            System.out.println("✅ Imported " + txs.size() + " transactions.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


}