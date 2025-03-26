package com.finance.notification;

import com.finance.domain.Transaction;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.*;

public class ScheduledReminderService implements ReminderService {
    private final ScheduledExecutorService scheduler;

    // ✅ Constructor accepting pool size
    public ScheduledReminderService(int poolSize) {
        this.scheduler = Executors.newScheduledThreadPool(poolSize);
    }

    @Override
    public void schedule(Transaction transaction) {
        // In real cases, you'd use dueDate, for demo we use LocalDate.now() + 10 sec
        LocalDate targetDate = transaction.getDate().plusDays(1);

        long delay = ChronoUnit.SECONDS.between(LocalDate.now().atStartOfDay(), targetDate.atStartOfDay());

        scheduler.schedule(() -> {
            System.out.printf("🔔 Reminder: '%s' of %.2f for Account %d is due today!%n",
                    transaction.getDescription(),
                    transaction.getAmount(),
                    transaction.getAccountId());
        }, delay, TimeUnit.SECONDS);
    }

    @Override
    public void shutdown() {
        scheduler.shutdown();
    }
}
