package com.example.tp1jeeghalilahlou.mappers;

import com.example.tp1jeeghalilahlou.dto.BankAccountRequestDTO;
import com.example.tp1jeeghalilahlou.dto.BankAccountResponseDTO;
import com.example.tp1jeeghalilahlou.entities.BankAccount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "active", ignore = true)
    BankAccount toEntity(BankAccountRequestDTO dto);
    
    BankAccountResponseDTO toResponseDTO(BankAccount entity);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "active", ignore = true)
    BankAccount updateEntity(BankAccountRequestDTO dto, @MappingTarget BankAccount entity);
}
