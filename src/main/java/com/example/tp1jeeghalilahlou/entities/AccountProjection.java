package com.example.tp1jeeghalilahlou.entities;

import org.springframework.data.rest.core.config.Projection;

import java.util.Date;

@Projection(name = "accountProjection", types = { BankAccount.class })
public interface AccountProjection {
    String getId();
    Date getCreatedAt();
    Double getBalance();
    String getCurrency();
    String getOwner();
    Boolean getActive();
}
