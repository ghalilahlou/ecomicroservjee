package com.example.tp1jeeghalilahlou.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class TransferRequestDTO {
    private String sourceAccountId;
    private String destinationAccountId;
    private Double amount;
}
