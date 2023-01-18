package com.rroager.walletservice.controller;

import com.rroager.walletservice.entity.Wallet;
import com.rroager.walletservice.service.TransactionService;
import com.rroager.walletservice.service.WalletService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {
    private final WalletService walletService;
    private final TransactionService transactionService;

    public WalletController(WalletService walletService, TransactionService transactionService) {
        this.walletService = walletService;
        this.transactionService = transactionService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Wallet> getWalletById(@PathVariable UUID id) {
        return new ResponseEntity<>(walletService.getWalletById(id), HttpStatus.OK);
    }

    @PostMapping("/create-wallet")
    public UUID createWallet() {
        return walletService.createWallet().getId();
    }
}
