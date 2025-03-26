package com.finance.domain;

import java.io.Serializable;
import java.math.BigDecimal;

public class Account extends BaseEntity<Long> implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private BigDecimal balance;

    public Account(Long id, String name) {
        this.id = id;
        this.name = name;
        this.balance = BigDecimal.ZERO;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void deposit(BigDecimal amount) {
        if (amount.signum() <= 0) throw new IllegalArgumentException("Amount must be positive");
        balance = balance.add(amount);
    }

    public void withdraw(BigDecimal amount) {
        if (amount.signum() <= 0) throw new IllegalArgumentException("Amount must be positive");
        if (balance.compareTo(amount) < 0) throw new IllegalStateException("Insufficient funds");
        balance = balance.subtract(amount);
    }

    @Override
    public String toString() {
        return String.format("Account{id=%d, name='%s', balance=%.2f}", id, name, balance);
    }

}
