package com.rroager.transactionservice.service;

import com.rroager.transactionservice.entity.Transaction;
import com.rroager.transactionservice.feign.FeignClient;
import com.rroager.transactionservice.repository.TransactionRepository;
import com.rroager.transactionservice.response.WalletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class TransactionService {

    Logger logger = LoggerFactory.getLogger(TransactionService.class);
    private final TransactionRepository transactionRepository;
    private final FeignClient feignClient;

    public TransactionService(TransactionRepository transactionRepository, FeignClient feignClient) {
        this.transactionRepository = transactionRepository;
        this.feignClient = feignClient;
    }

    /**
     *
     * @param id (Integer)
     * @return Transaction
     * Retrieves transaction based on id
     * If no transaction exists with the id, returns null
     */
    public Transaction getTransactionByIdAndWalletId(Integer id, Integer walletId) {
        logger.info("(getTransactionByIdAndWalletId) Getting transaction with ID: " + id + ". For wallet with ID: " + walletId);

        return transactionRepository.findByIdAndWalletId(id, walletId).orElse(null);
    }

    /**
     *
     * @param walletId (Integer)
     * @return List<Transaction>
     * Retrieves all transactions for a specific walletId
     */
    public List<Transaction> getAllTransactionsWalletId(Integer walletId) {
        logger.info("(getTransactionsWalletId) Getting transactions for wallet with ID: " + walletId);

        return transactionRepository.findAllByWalletId(walletId);
    }

    /**
     *
     * @param walletId (Integer)
     * @param fromDate (Date)
     * @param toDate (Date)
     * @return List<Transaction>
     * Retrieves all transactions for a specific walletId between two dates
     */
    public List<Transaction> getAllByWalletIdFromDateToDate(Integer walletId, Date fromDate, Date toDate) {
        logger.info("(getTransactionsWalletId) Getting transactions for wallet with ID: " + walletId);

        return transactionRepository.findAllByWalletIdFromDateToDate(walletId, fromDate, toDate);
    }

    /**
     *
     * @param walletId (Integer)
     * @param transaction (Transaction)
     * @return Transaction
     * Sends transaction to WalletService for wallet balance to be updated
     * If wallet does not exist or has insufficient funds null is received as well as a HttpStatus which is handled by the GlobalExceptionHandler
     * Creates new transaction for a specific walletId with details given and saves to db
     */
    public Transaction createTransaction(Integer walletId, Transaction transaction) {
        transaction.setWalletId(walletId);
        WalletResponse walletResponse = feignClient.updateWalletBalance(transaction).getBody();
        if (walletResponse != null) {
            logger.info("(createTransaction) Creating transaction for wallet with ID: " + walletId);

            return transactionRepository.save(new Transaction(transaction.getWalletId(), transaction.getAmount(), walletResponse.getBalance(), transaction.getTransactionType()));
        }
        return null;
    }
}
