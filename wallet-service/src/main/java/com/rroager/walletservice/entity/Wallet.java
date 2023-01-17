package com.rroager.walletservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Wallet {
    @Id
    private String id;

    private Double balance;

    @OneToMany
    private List<Transaction> transactions;

    // For new wallets TODO maybe create a CreateWalletRequest model
    public Wallet(String id) {
        this.id = id;
        this.balance = 0.0;
        this.transactions = new ArrayList<>();
    }
}
