package com.example.tp1jeeghalilahlou.repositories;

import com.example.tp1jeeghalilahlou.entities.BankAccount;
import com.example.tp1jeeghalilahlou.enums.AccountType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class BankAccountRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    private BankAccount account1;
    private BankAccount account2;
    private BankAccount account3;

    @BeforeEach
    void setUp() {
        account1 = BankAccount.builder()
                .id("acc-001")
                .createdAt(new Date())
                .balance(1000.0)
                .currency("EUR")
                .type(AccountType.CURRENT_ACCOUNT)
                .owner("John Doe")
                .active(true)
                .build();

        account2 = BankAccount.builder()
                .id("acc-002")
                .createdAt(new Date())
                .balance(5000.0)
                .currency("EUR")
                .type(AccountType.SAVING_ACCOUNT)
                .owner("Jane Smith")
                .active(true)
                .build();

        account3 = BankAccount.builder()
                .id("acc-003")
                .createdAt(new Date())
                .balance(2500.0)
                .currency("USD")
                .type(AccountType.CURRENT_ACCOUNT)
                .owner("John Doe")
                .active(false)
                .build();

        entityManager.persistAndFlush(account1);
        entityManager.persistAndFlush(account2);
        entityManager.persistAndFlush(account3);
    }

    @Test
    void testFindByType() {
        List<BankAccount> currentAccounts = bankAccountRepository.findByType(AccountType.CURRENT_ACCOUNT);
        assertThat(currentAccounts).hasSize(2);
    }

    @Test
    void testFindByOwner() {
        List<BankAccount> johnAccounts = bankAccountRepository.findByOwner("John Doe");
        assertThat(johnAccounts).hasSize(2);
    }

    @Test
    void testFindByActiveTrue() {
        List<BankAccount> activeAccounts = bankAccountRepository.findByActiveTrue();
        assertThat(activeAccounts).hasSize(2);
    }

    @Test
    void testFindByIdAndActiveTrue() {
        Optional<BankAccount> activeAccount = bankAccountRepository.findByIdAndActiveTrue("acc-001");
        assertThat(activeAccount).isPresent();
        assertThat(activeAccount.get().getOwner()).isEqualTo("John Doe");

        Optional<BankAccount> inactiveAccount = bankAccountRepository.findByIdAndActiveTrue("acc-003");
        assertThat(inactiveAccount).isEmpty();
    }
}
