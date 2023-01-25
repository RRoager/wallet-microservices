package com.rroager.walletservice.request;

import lombok.*;

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
