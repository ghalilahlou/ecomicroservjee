package com.example.tp1jeeghalilahlou.dto;

import com.example.tp1jeeghalilahlou.enums.AccountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class BankAccountResponseDTO {
    private String id;
    private Date createdAt;
    private Double balance;
    private String currency;
    private AccountType type;
    private String owner;
    private Boolean active;
}
