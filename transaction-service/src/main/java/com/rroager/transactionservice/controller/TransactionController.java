package com.rroager.transactionservice.controller;

import com.rroager.transactionservice.entity.Transaction;
import com.rroager.transactionservice.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/wallet/{walletId}/{id}")
    public ResponseEntity<Transaction> getTransactionByIdAndWalletId(@PathVariable Integer walletId, @PathVariable Integer id) {
        return new ResponseEntity<>(transactionService.getTransactionByIdAndWalletId(id, walletId), HttpStatus.OK);
    }

    @GetMapping("/wallet/{walletId}")
    public ResponseEntity<List<Transaction>> getAllTransactionsByWalletId(@PathVariable Integer walletId) {
        return new ResponseEntity<>(transactionService.getAllTransactionsWalletId(walletId), HttpStatus.OK);
    }

    @GetMapping("/wallet/{walletId}/from/{fromDate}/to/{toDate}")
    public ResponseEntity<List<Transaction>> getAllByWalletIdFromDateToDate(@PathVariable Integer walletId, @PathVariable Date fromDate, @PathVariable Date toDate) {
        return new ResponseEntity<>(transactionService.getAllByWalletIdFromDateToDate(walletId, fromDate, toDate), HttpStatus.OK);
    }

    @PostMapping("/wallet/{walletId}/create-transaction")
    public ResponseEntity<?> createTransaction(@PathVariable Integer walletId, @RequestBody Transaction transaction) {
        if (transaction.getAmount() <= 0) {
            return new ResponseEntity<>("Transaction amount must be more than 0.", HttpStatus.BAD_REQUEST);
        }

        try {
            return new ResponseEntity<>(transactionService.createTransaction(walletId, transaction), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Insufficient funds in wallet.", HttpStatus.BAD_REQUEST);
        }
    }
}
