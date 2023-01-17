package com.rroager.userservice.response;

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

    private Long amount;

    private Date transactionDate;

    private TransactionType transactionType;
}
