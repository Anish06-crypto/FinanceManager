
import com.finance.domain.Account;
import com.finance.domain.Transaction;
import com.finance.repository.Repository;
import com.finance.service.AccountService;
import com.finance.service.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AccountServiceImplTest {

    private Repository<Account> accountRepo;
    private Repository<Transaction> transactionRepo;
    private AccountService accountService;

    @BeforeEach
    void setup() {
        accountRepo = mock(Repository.class);
        transactionRepo = mock(Repository.class);
        accountService = new AccountServiceImpl(accountRepo, transactionRepo);
    }

    @Test
    void createAccount_shouldGenerateAccountWithValidId() {
        Account account = accountService.createAccount("Krat6s");
        assertNotNull(account.getId());
        assertEquals("Krat6s", account.getName());
        verify(accountRepo).save(any(Account.class));
    }

    @Test
    void deleteAccount_shouldFailIfTransactionsExist() {
        long accountId = 42L;
        when(transactionRepo.findAll()).thenReturn(
                Collections.singletonList(new Transaction(1L, accountId, "tx", null, 100.0))
        );
        when(accountRepo.findById(accountId)).thenReturn(new Account(accountId, "Blocked"));

        Exception ex = assertThrows(IllegalStateException.class,
                () -> accountService.deleteAccount(accountId));

        assertTrue(ex.getMessage().contains("existing transactions"));
    }
}
