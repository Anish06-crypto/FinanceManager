package com.finance.domain;

import java.io.Serializable;
import java.time.LocalDate;

public class Transaction extends BaseEntity<Long> implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long accountId;
    private String description;
    private LocalDate date;
    private double amount;

    public Transaction(Long id, Long accountId, String description, LocalDate date, double amount) {
        this.id = id;
        this.accountId = accountId;
        this.description = description;
        this.date = date;
        this.amount = amount;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return String.format(
                "Transaction{id=%d, accountId=%d, desc='%s', date=%s, amount=%.2f}",
                id, accountId, description, date, amount
        );
    }

}
