package com.finance.notification;

import com.finance.domain.Transaction;

public interface ReminderService {
    void schedule(Transaction transaction);
    void shutdown(); // for graceful shutdown on exit
}
