import com.finance.domain.Account;
import com.finance.domain.Transaction;
import com.finance.notification.ReminderService;
import com.finance.repository.Repository;
import com.finance.service.TransactionService;
import com.finance.service.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class TransactionServiceImplTest {

    private Repository<Transaction> transactionRepo;
    private Repository<Account> accountRepo;
    private ReminderService reminderService;
    private TransactionService transactionService;

    private final Account testAccount = new Account(1L, "TestAccount");

    @BeforeEach
    void setUp() {
        transactionRepo = mock(Repository.class);
        accountRepo = mock(Repository.class);
        reminderService = mock(ReminderService.class);

        transactionService = new TransactionServiceImpl(transactionRepo, accountRepo, reminderService);

        when(accountRepo.findById(1L)).thenReturn(testAccount);
    }

    @Test
    void recordTransaction_shouldCreateTransactionAndUpdateBalance() {
        double initialBalance = testAccount.getBalance().doubleValue();
        double amount = 2000.00;

        Transaction recorded = transactionService.recordTransaction(null, 1L, "Salary", amount);

        assertNotNull(recorded);
        assertEquals("Salary", recorded.getDescription());
        assertEquals(amount, recorded.getAmount(), 0.01);

        verify(transactionRepo).save(any(Transaction.class));
        verify(accountRepo).update(testAccount);
        verify(reminderService).schedule(any(Transaction.class));

        assertEquals(initialBalance + amount, testAccount.getBalance().doubleValue(), 0.01);
    }

    @Test
    void getTransactionsForAccount_shouldFilterByAccountId() {
        List<Transaction> mockList = new ArrayList<>();
        mockList.add(new Transaction(1L, 1L, "Salary", LocalDate.now(), 1000));
        mockList.add(new Transaction(2L, 2L, "IgnoreMe", LocalDate.now(), 500));

        when(transactionRepo.findAll()).thenReturn(mockList);

        List<Transaction> result = transactionService.getTransactionsForAccount(1L);
        assertEquals(1, result.size());
        assertEquals("Salary", result.get(0).getDescription());
    }
}
