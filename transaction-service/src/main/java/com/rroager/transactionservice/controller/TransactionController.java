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

    @GetMapping("/{walletId}/{id}")
    public ResponseEntity<Transaction> getTransactionByIdAndWalletId(@PathVariable Integer walletId, @PathVariable Integer id) {
        return new ResponseEntity<>(transactionService.getTransactionByIdAndWalletId(id, walletId), HttpStatus.OK);
    }

    @GetMapping("/{walletId}")
    public ResponseEntity<List<Transaction>> getAllTransactionsByWalletId(@PathVariable Integer walletId) {
        return new ResponseEntity<>(transactionService.getAllTransactionsWalletId(walletId), HttpStatus.OK);
    }

    @GetMapping("/{walletId}/from/{fromDate}/to/{toDate}")
    public ResponseEntity<List<Transaction>> getAllByWalletIdFromDateToDate(@PathVariable Integer walletId, @PathVariable Date fromDate, @PathVariable Date toDate) {
        return new ResponseEntity<>(transactionService.getAllByWalletIdFromDateToDate(walletId, fromDate, toDate), HttpStatus.OK);
    }

    @PostMapping("/{walletId}/create-transaction")
    public ResponseEntity<Transaction> createTransaction(@PathVariable Integer walletId, @RequestBody Transaction transaction) {
        return new ResponseEntity<>(transactionService.createTransaction(walletId, transaction), HttpStatus.CREATED);
    }

    // TODO DEPOSIT or WITHDRAW from wallet
}
