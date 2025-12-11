package com.example.tp1jeeghalilahlou.web;

import com.example.tp1jeeghalilahlou.dto.BankAccountRequestDTO;
import com.example.tp1jeeghalilahlou.dto.BankAccountResponseDTO;
import com.example.tp1jeeghalilahlou.dto.TransferRequestDTO;
import com.example.tp1jeeghalilahlou.entities.BankAccount;
import com.example.tp1jeeghalilahlou.repositories.BankAccountRepository;
import com.example.tp1jeeghalilahlou.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Account Controller", description = "API for bank account management")
public class AccountRestController {
    
    private final BankAccountRepository bankAccountRepository;
    private final AccountService accountService;

    @Operation(summary = "Get all bank accounts")
    @GetMapping("/bankAccounts")
    public List<BankAccount> bankAccounts() {
        return bankAccountRepository.findAll();
    }

    @Operation(summary = "Get bank account by ID")
    @GetMapping("/bankAccounts/{id}")
    public BankAccount bankAccount(@PathVariable String id) {
        return bankAccountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(String.format("Account %s not found", id)));
    }

    @Operation(summary = "Create a new bank account")
    @PostMapping("/bankAccounts")
    public BankAccountResponseDTO save(@RequestBody BankAccountRequestDTO requestDTO) {
        return accountService.addAccount(requestDTO);
    }

    @Operation(summary = "Update bank account")
    @PutMapping("/bankAccounts/{id}")
    public BankAccountResponseDTO update(@PathVariable String id, @RequestBody BankAccountRequestDTO requestDTO) {
        return accountService.updateAccount(id, requestDTO);
    }

    @Operation(summary = "Delete bank account")
    @DeleteMapping("/bankAccounts/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable String id) {
        accountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get accounts by owner")
    @GetMapping("/bankAccounts/owner/{owner}")
    public List<BankAccountResponseDTO> getAccountsByOwner(@PathVariable String owner) {
        return accountService.getAccountsByOwner(owner);
    }

    @Operation(summary = "Get accounts by type")
    @GetMapping("/bankAccounts/type/{type}")
    public List<BankAccountResponseDTO> getAccountsByType(@PathVariable String type) {
        return accountService.getAccountsByType(type);
    }

    @Operation(summary = "Get active accounts")
    @GetMapping("/bankAccounts/active")
    public List<BankAccountResponseDTO> getActiveAccounts() {
        return accountService.getActiveAccounts();
    }

    @Operation(summary = "Transfer money between accounts")
    @PostMapping("/bankAccounts/transfer")
    public ResponseEntity<String> transferMoney(@RequestBody TransferRequestDTO transferRequest) {
        try {
            String result = accountService.transferMoney(transferRequest);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Transfer failed: " + e.getMessage());
        }
    }
}
