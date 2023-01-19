package com.rroager.transactionservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private UUID walletId;
    private Double amount;
    private Date transactionDate;
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    public Transaction(UUID walletId, Double amount, TransactionType transactionType) {
        this.walletId = walletId;
        this.amount = amount;
        this.transactionDate = new Date(System.currentTimeMillis());
        this.transactionType = transactionType;
    }
}
