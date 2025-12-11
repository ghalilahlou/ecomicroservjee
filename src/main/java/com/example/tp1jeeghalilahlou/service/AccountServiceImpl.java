package com.example.tp1jeeghalilahlou.service;

import com.example.tp1jeeghalilahlou.dto.BankAccountRequestDTO;
import com.example.tp1jeeghalilahlou.dto.BankAccountResponseDTO;
import com.example.tp1jeeghalilahlou.dto.TransferRequestDTO;
import com.example.tp1jeeghalilahlou.entities.BankAccount;
import com.example.tp1jeeghalilahlou.enums.AccountType;
import com.example.tp1jeeghalilahlou.mappers.AccountMapper;
import com.example.tp1jeeghalilahlou.repositories.BankAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountServiceImpl implements AccountService {

    private final BankAccountRepository bankAccountRepository;
    private final AccountMapper accountMapper;

    @Override
    public BankAccountResponseDTO addAccount(BankAccountRequestDTO requestDTO) {
        BankAccount account = accountMapper.toEntity(requestDTO);
        // Set generated fields
        account.setId(java.util.UUID.randomUUID().toString());
        account.setCreatedAt(new java.util.Date());
        account.setActive(true);
        BankAccount savedAccount = bankAccountRepository.save(account);
        return accountMapper.toResponseDTO(savedAccount);
    }

    @Override
    public BankAccountResponseDTO updateAccount(String id, BankAccountRequestDTO requestDTO) {
        BankAccount existingAccount = bankAccountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + id));
        
        accountMapper.updateEntity(requestDTO, existingAccount);
        BankAccount updatedAccount = bankAccountRepository.save(existingAccount);
        return accountMapper.toResponseDTO(updatedAccount);
    }

    @Override
    public void deleteAccount(String id) {
        if (!bankAccountRepository.existsById(id)) {
            throw new RuntimeException("Account not found with id: " + id);
        }
        bankAccountRepository.deleteById(id);
    }

    @Override
    public BankAccountResponseDTO getAccountById(String id) {
        BankAccount account = bankAccountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + id));
        return accountMapper.toResponseDTO(account);
    }

    @Override
    public List<BankAccountResponseDTO> getAllAccounts() {
        return bankAccountRepository.findAll()
                .stream()
                .map(accountMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BankAccountResponseDTO> getAccountsByOwner(String owner) {
        return bankAccountRepository.findByOwner(owner)
                .stream()
                .map(accountMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BankAccountResponseDTO> getAccountsByType(String type) {
        AccountType accountType = AccountType.valueOf(type.toUpperCase());
        return bankAccountRepository.findByType(accountType)
                .stream()
                .map(accountMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BankAccountResponseDTO> getActiveAccounts() {
        return bankAccountRepository.findByActiveTrue()
                .stream()
                .map(accountMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public String transferMoney(TransferRequestDTO transferRequest) {
        BankAccount sourceAccount = bankAccountRepository.findByIdAndActiveTrue(transferRequest.getSourceAccountId())
                .orElseThrow(() -> new RuntimeException("Source account not found or inactive"));
        
        BankAccount destinationAccount = bankAccountRepository.findByIdAndActiveTrue(transferRequest.getDestinationAccountId())
                .orElseThrow(() -> new RuntimeException("Destination account not found or inactive"));

        if (sourceAccount.getBalance() < transferRequest.getAmount()) {
            throw new RuntimeException("Insufficient balance");
        }

        if (transferRequest.getAmount() <= 0) {
            throw new RuntimeException("Transfer amount must be positive");
        }

        // Perform transfer
        sourceAccount.setBalance(sourceAccount.getBalance() - transferRequest.getAmount());
        destinationAccount.setBalance(destinationAccount.getBalance() + transferRequest.getAmount());

        bankAccountRepository.save(sourceAccount);
        bankAccountRepository.save(destinationAccount);

        return "Transfer completed successfully";
    }
}
