package com.finance.service;
import com.finance.domain.Account;
import java.math.BigDecimal;
import java.util.List;

public interface AccountService {
    Account createAccount(String name);
    void deposit(Long accountId, BigDecimal amount);
    void withdraw(Long accountId, BigDecimal amount);
    List<Account> getAllAccounts();
    void deleteAccount(Long id);
}