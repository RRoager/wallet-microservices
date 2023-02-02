package com.rroager.transactionservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer walletId;
    private Double amount;
    private Double currentBalance;
    private Date transactionDate;
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    public Transaction(Integer walletId, Double amount, Double currentBalance, TransactionType transactionType) {
        this.walletId = walletId;
        this.amount = amount;
        this.currentBalance = currentBalance;
        this.transactionDate = new Date(System.currentTimeMillis());
        this.transactionType = transactionType;
    }
}
