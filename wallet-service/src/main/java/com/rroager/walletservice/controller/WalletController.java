package com.rroager.walletservice.controller;

import com.rroager.walletservice.entity.Wallet;
import com.rroager.walletservice.request.TransactionRequest;
import com.rroager.walletservice.service.WalletService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {
    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getWalletById(@PathVariable Integer id) {
        Wallet wallet = walletService.getWalletById(id);
        if (wallet != null) {
            return new ResponseEntity<>(wallet, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No wallet with ID: " + id, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/user/{userId}/create-wallet")
    public ResponseEntity<Wallet> createWallet(@PathVariable Integer userId) {
        return new ResponseEntity<>(walletService.createWallet(userId), HttpStatus.CREATED);
    }

    @PutMapping("/update-wallet")
    public ResponseEntity<?> updateWalletBalance(@RequestBody TransactionRequest transactionRequest) {
        Wallet wallet = walletService.getWalletById(transactionRequest.getWalletId());
        if (wallet != null) {
            Wallet updatedWallet = walletService.updateWalletBalance(transactionRequest, wallet);

            if (updatedWallet == null) {
                return new ResponseEntity<>("Insufficient funds in wallet.", HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<>(wallet, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No wallet with ID: " + transactionRequest.getWalletId(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}/delete-wallet")
    public ResponseEntity<String> deleteWallet(@PathVariable Integer id) {
        if (walletService.deleteWallet(id)) {
            return new ResponseEntity<>("Deleted wallet with ID: " + id, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Not able to delete wallet. No wallet with ID: " + id, HttpStatus.NOT_FOUND);
        }
    }
}
