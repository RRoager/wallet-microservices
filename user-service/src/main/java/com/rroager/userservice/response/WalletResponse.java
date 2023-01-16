package com.rroager.userservice.response;

import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WalletResponse {

    private String walletId;

    private Long balance;

    @OneToMany
    private List<TransactionResponse> transactions;
}
