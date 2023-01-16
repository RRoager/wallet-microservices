package com.rroager.userservice.response;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {
    public enum TransactionType { DEPOSIT, WITHDRAW }

    private Long transactionId;

    private long amount;

    private Date date;

    private TransactionType transactionType;
}
