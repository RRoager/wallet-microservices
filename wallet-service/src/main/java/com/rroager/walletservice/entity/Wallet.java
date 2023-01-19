package com.rroager.walletservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Wallet {
    @Id
    private UUID id;
    private Double balance;

    public Wallet(UUID id) {
        this.id = id;
        this.balance = 0.0;
    }
}
