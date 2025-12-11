package com.example.tp1jeeghalilahlou.repositories;

import com.example.tp1jeeghalilahlou.entities.BankAccount;
import com.example.tp1jeeghalilahlou.enums.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource
public interface BankAccountRepository extends JpaRepository<BankAccount, String> {
    
    @RestResource(path = "/byType")
    List<BankAccount> findByType(@Param("t") AccountType type);
    
    @RestResource(path = "/byOwner")
    List<BankAccount> findByOwner(@Param("owner") String owner);
    
    @RestResource(path = "/active")
    List<BankAccount> findByActiveTrue();
    
    Optional<BankAccount> findByIdAndActiveTrue(String id);
}
