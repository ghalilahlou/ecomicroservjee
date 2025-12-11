package com.example.tp1jeeghalilahlou.web;

import com.example.tp1jeeghalilahlou.dto.BankAccountRequestDTO;
import com.example.tp1jeeghalilahlou.dto.BankAccountResponseDTO;
import com.example.tp1jeeghalilahlou.dto.TransferRequestDTO;
import com.example.tp1jeeghalilahlou.entities.BankAccount;
import com.example.tp1jeeghalilahlou.enums.AccountType;
import com.example.tp1jeeghalilahlou.repositories.BankAccountRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Transactional
public class AccountRestControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        bankAccountRepository.deleteAll();
    }

    @Test
    void testCreateAccount() throws Exception {
        BankAccountRequestDTO requestDTO = BankAccountRequestDTO.builder()
                .balance(1000.0)
                .currency("EUR")
                .type(AccountType.CURRENT_ACCOUNT)
                .owner("John Doe")
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<BankAccountRequestDTO> request = new HttpEntity<>(requestDTO, headers);

        ResponseEntity<BankAccountResponseDTO> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/bankAccounts", 
                request, 
                BankAccountResponseDTO.class
        );

        assert response.getStatusCode().is2xxSuccessful();
        assert response.getBody() != null;
        assert response.getBody().getBalance().equals(1000.0);
        assert response.getBody().getCurrency().equals("EUR");
    }

    @Test
    void testGetAllAccounts() throws Exception {
        // Create test accounts
        BankAccount account1 = BankAccount.builder()
                .id("acc-001")
                .createdAt(new Date())
                .balance(1000.0)
                .currency("EUR")
                .type(AccountType.CURRENT_ACCOUNT)
                .owner("John Doe")
                .active(true)
                .build();

        BankAccount account2 = BankAccount.builder()
                .id("acc-002")
                .createdAt(new Date())
                .balance(5000.0)
                .currency("EUR")
                .type(AccountType.SAVING_ACCOUNT)
                .owner("Jane Smith")
                .active(true)
                .build();

        bankAccountRepository.save(account1);
        bankAccountRepository.save(account2);

        ResponseEntity<BankAccount[]> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/bankAccounts", 
                BankAccount[].class
        );

        assert response.getStatusCode().is2xxSuccessful();
        assert response.getBody() != null;
        assert response.getBody().length == 2;
    }

    @Test
    void testGetAccountById() throws Exception {
        BankAccount account = BankAccount.builder()
                .id("acc-001")
                .createdAt(new Date())
                .balance(1000.0)
                .currency("EUR")
                .type(AccountType.CURRENT_ACCOUNT)
                .owner("John Doe")
                .active(true)
                .build();

        bankAccountRepository.save(account);

        ResponseEntity<BankAccount> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/bankAccounts/acc-001", 
                BankAccount.class
        );

        assert response.getStatusCode().is2xxSuccessful();
        assert response.getBody() != null;
        assert response.getBody().getId().equals("acc-001");
        assert response.getBody().getBalance().equals(1000.0);
    }

    @Test
    void testTransferMoney() throws Exception {
        // Create source and destination accounts
        BankAccount sourceAccount = BankAccount.builder()
                .id("acc-001")
                .createdAt(new Date())
                .balance(1000.0)
                .currency("EUR")
                .type(AccountType.CURRENT_ACCOUNT)
                .owner("John Doe")
                .active(true)
                .build();

        BankAccount destinationAccount = BankAccount.builder()
                .id("acc-002")
                .createdAt(new Date())
                .balance(500.0)
                .currency("EUR")
                .type(AccountType.SAVING_ACCOUNT)
                .owner("Jane Smith")
                .active(true)
                .build();

        bankAccountRepository.save(sourceAccount);
        bankAccountRepository.save(destinationAccount);

        TransferRequestDTO transferRequest = TransferRequestDTO.builder()
                .sourceAccountId("acc-001")
                .destinationAccountId("acc-002")
                .amount(200.0)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<TransferRequestDTO> request = new HttpEntity<>(transferRequest, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/bankAccounts/transfer", 
                request, 
                String.class
        );

        assert response.getStatusCode().is2xxSuccessful();
        assert response.getBody() != null;
        assert response.getBody().equals("Transfer completed successfully");
    }
}
