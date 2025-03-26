package com.finance.util;

import com.finance.domain.Transaction;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CsvUtil {

    public static void exportTransactionsToCsv(List<Transaction> transactions, String filePath) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.println("id,accountId,description,date,amount");

            for (Transaction tx : transactions) {
                writer.printf("%d,%d,%s,%s,%.2f%n",
                        tx.getId(),
                        tx.getAccountId(),
                        tx.getDescription().replace(",", " "), // remove commas
                        tx.getDate(),
                        tx.getAmount()
                );
            }
        }
    }

    public static List<Transaction> importTransactionsFromCsv(String filePath) throws IOException {
        List<Transaction> transactions = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine(); // skip header
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");

                Long id = Long.parseLong(fields[0]);
                Long accountId = Long.parseLong(fields[1]);
                String description = fields[2];
                LocalDate date = LocalDate.parse(fields[3]);
                double amount = Double.parseDouble(fields[4]);

                transactions.add(new Transaction(id, accountId, description, date, amount));
            }
        }

        return transactions;
    }
}
