package com.example.tp1jeeghalilahlou.graphql;

import com.example.tp1jeeghalilahlou.dto.BankAccountRequestDTO;
import com.example.tp1jeeghalilahlou.dto.BankAccountResponseDTO;
import com.example.tp1jeeghalilahlou.dto.TransferRequestDTO;
import com.example.tp1jeeghalilahlou.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AccountGraphQLController {

    private final AccountService accountService;

    @QueryMapping
    public List<BankAccountResponseDTO> bankAccounts() {
        return accountService.getAllAccounts();
    }

    @QueryMapping
    public BankAccountResponseDTO bankAccount(@Argument String id) {
        return accountService.getAccountById(id);
    }

    @QueryMapping
    public List<BankAccountResponseDTO> bankAccountsByOwner(@Argument String owner) {
        return accountService.getAccountsByOwner(owner);
    }

    @QueryMapping
    public List<BankAccountResponseDTO> bankAccountsByType(@Argument String type) {
        return accountService.getAccountsByType(type);
    }

    @QueryMapping
    public List<BankAccountResponseDTO> activeBankAccounts() {
        return accountService.getActiveAccounts();
    }

    @MutationMapping
    public BankAccountResponseDTO createBankAccount(@Argument BankAccountRequestDTO input) {
        return accountService.addAccount(input);
    }

    @MutationMapping
    public BankAccountResponseDTO updateBankAccount(@Argument String id, @Argument BankAccountRequestDTO input) {
        return accountService.updateAccount(id, input);
    }

    @MutationMapping
    public Boolean deleteBankAccount(@Argument String id) {
        try {
            accountService.deleteAccount(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @MutationMapping
    public String transferMoney(@Argument TransferRequestDTO transferRequest) {
        try {
            return accountService.transferMoney(transferRequest);
        } catch (Exception e) {
            return "Transfer failed: " + e.getMessage();
        }
    }
}
