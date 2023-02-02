package com.rroager.walletservice.request;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class TransactionRequest {
    public enum TransactionType { DEPOSIT, WITHDRAW }

    private Integer walletId;
    private Double amount;
    private TransactionType transactionType;
}
