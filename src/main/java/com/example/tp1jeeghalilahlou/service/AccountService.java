package com.example.tp1jeeghalilahlou.service;

import com.example.tp1jeeghalilahlou.dto.BankAccountRequestDTO;
import com.example.tp1jeeghalilahlou.dto.BankAccountResponseDTO;
import com.example.tp1jeeghalilahlou.dto.TransferRequestDTO;

import java.util.List;

public interface AccountService {
    BankAccountResponseDTO addAccount(BankAccountRequestDTO requestDTO);
    BankAccountResponseDTO updateAccount(String id, BankAccountRequestDTO requestDTO);
    void deleteAccount(String id);
    BankAccountResponseDTO getAccountById(String id);
    List<BankAccountResponseDTO> getAllAccounts();
    List<BankAccountResponseDTO> getAccountsByOwner(String owner);
    List<BankAccountResponseDTO> getAccountsByType(String type);
    List<BankAccountResponseDTO> getActiveAccounts();
    String transferMoney(TransferRequestDTO transferRequest);
}
