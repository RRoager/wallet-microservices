package com.rroager.transactionservice.service;

import com.rroager.transactionservice.entity.Transaction;
import com.rroager.transactionservice.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

@Service
public class TransactionService {

    Logger logger = LoggerFactory.getLogger(TransactionService.class);
    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    /**
     *
     * @param id (Integer)
     * @return Transaction
     * Retrieves transaction based on id
     * If no transaction exists with the id, returns null
     */
    public Transaction getTransactionByIdAndWalletId(Integer id, UUID walletId) {
        logger.info("(getTransactionByIdAndWalletId) Getting transaction with ID: " + id + ". For wallet with ID: " + walletId);

        return transactionRepository.findByIdAndWalletId(id, walletId).orElse(null);
    }

    /**
     *
     * @param walletId (UUID)
     * @return List<Transaction>
     * Retrieves all transactions for a specific walletId
     */
    public List<Transaction> getAllTransactionsWalletId(UUID walletId) {
        logger.info("(getTransactionsWalletId) Getting transactions for wallet with ID: " + walletId);

        return transactionRepository.findAllByWalletId(walletId);
    }

    /**
     *
     * @param walletId (UUID)
     * @param fromDate (Date)
     * @param toDate (Date)
     * @return List<Transaction>
     * Retrieves all transactions for a specific walletId between two dates
     */
    public List<Transaction> getAllByWalletIdFromDateToDate(UUID walletId, Date fromDate, Date toDate) {
        logger.info("(getTransactionsWalletId) Getting transactions for wallet with ID: " + walletId);

        return transactionRepository.findAllByWalletIdFromDateToDate(walletId, fromDate, toDate);
    }

    /**
     *
     * @param walletId (UUID)
     * @param transaction (Transaction)
     * @return Transaction
     * Creates new transaction for a specific walletId with details given
     */
    public Transaction createTransaction(UUID walletId, Transaction transaction) {
        logger.info("(createTransaction) Creating transaction for wallet with ID: " + walletId);

        return transactionRepository.save(new Transaction(transaction.getWalletId(), transaction.getAmount(), transaction.getTransactionType()));
    }
}