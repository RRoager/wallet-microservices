package com.rroager.walletservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "wallets")
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer userId; // TODO find where this should be added
    private Double balance;

    public Wallet(Integer userId) {
        this.userId = userId;
        this.balance = 0.0;
    }
}
