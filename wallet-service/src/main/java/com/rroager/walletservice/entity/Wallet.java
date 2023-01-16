package com.rroager.walletservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Wallet {
    @Id
    private String id;

    private Long balance;

    @OneToMany
    private List<Transaction> transactions;
}
