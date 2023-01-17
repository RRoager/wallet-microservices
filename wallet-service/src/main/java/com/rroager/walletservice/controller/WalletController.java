package com.rroager.walletservice.controller;

import com.rroager.walletservice.entity.Wallet;
import com.rroager.walletservice.service.TransactionService;
import com.rroager.walletservice.service.WalletService;
import org.springframework.web.bind.annotation.*;

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
    public Wallet getWalletById(@PathVariable String id) {
        return walletService.getWalletById(id);
    }

    @PostMapping("/create-wallet")
    public Wallet createWallet(@RequestBody Wallet wallet) {
        return walletService.createWallet(wallet);
    }
}
