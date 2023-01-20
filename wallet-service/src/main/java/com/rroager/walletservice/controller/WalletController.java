package com.rroager.walletservice.controller;

import com.rroager.walletservice.entity.Wallet;
import com.rroager.walletservice.request.TransactionRequest;
import com.rroager.walletservice.service.WalletService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {
    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Wallet> getWalletById(@PathVariable Integer id) {
        return new ResponseEntity<>(walletService.getWalletById(id), HttpStatus.OK);
    }

    @PostMapping("/create-wallet")
    public ResponseEntity<Wallet> createWallet() {
        return new ResponseEntity<>(walletService.createWallet(), HttpStatus.CREATED);
    }

    @PutMapping("/update-wallet")
    public ResponseEntity<String> updateWalletBalance(@RequestBody TransactionRequest transactionRequest) {
        Wallet wallet = walletService.updateWalletBalance(transactionRequest);

        if (wallet == null) {
            return new ResponseEntity<>("Insufficient funds in wallet", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("Updated balance of wallet with ID: " + wallet.getId(), HttpStatus.OK);
    }
}
