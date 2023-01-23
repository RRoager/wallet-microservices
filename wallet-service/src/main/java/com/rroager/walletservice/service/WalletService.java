package com.rroager.walletservice.service;

import com.rroager.walletservice.entity.Wallet;
import com.rroager.walletservice.repository.WalletRepository;
import com.rroager.walletservice.request.TransactionRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class WalletService {
    Logger logger = LoggerFactory.getLogger(WalletService.class);
    private final WalletRepository walletRepository;

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    /**
     *
     * @param id (Integer)
     * @return Wallet
     * Retrieves wallet based on wallet id
     * If no wallet exists with the id, returns null
     */
    public Wallet getWalletById(Integer id) {
        logger.info("(getWalletById) Getting wallet with ID: " + id);

        return walletRepository.findById(id).orElse(null);
    }

    /**
     *
     * @return Wallet
     * Creates new wallet and saves to db
     */
    public Wallet createWallet(Integer userId) {
        Wallet newWallet = walletRepository.save(new Wallet(userId));

        logger.info("(createWallet) Creating wallet with ID: " + newWallet.getId());

        return newWallet;
    }

    /**
     *
     * @param transactionRequest (TransactionRequest)
     * @return Wallet
     * Retrieves wallet with id matching the id from transactionRequest
     * Added transactionRequest amount to balance if DEPOSIT
     * Subtracts transactionRequest amount from balance if WITHDRAW and enough balance in wallet
     * Saves wallet with new balance
     */
    public Wallet updateWalletBalance(TransactionRequest transactionRequest) {
        Wallet wallet = getWalletById(transactionRequest.getWalletId());

        if (transactionRequest.getTransactionType().equals(TransactionRequest.TransactionType.DEPOSIT)) {
            wallet.setBalance(wallet.getBalance() + transactionRequest.getAmount());

            logger.info("(updateWalletBalance) Adding transaction amount to balance wallet with ID: " + wallet.getId());

            return walletRepository.save(wallet);
        } else if (transactionRequest.getTransactionType().equals(TransactionRequest.TransactionType.WITHDRAW)) {
            if (wallet.getBalance() < transactionRequest.getAmount()) {
                return null;
            }

            wallet.setBalance(wallet.getBalance() - transactionRequest.getAmount());

            logger.info("(updateWalletBalance) Subtracting transaction amount from balance wallet with ID: " + wallet.getId());

            return walletRepository.save(wallet);
        }

        return null;
    }

    /**
     *
     * @param id (Integer)
     * @return boolean
     * Retrieves wallet based on user ID
     * Deletes wallet from db
     * If wallet is null, no wallet exists with the ID and null is returned
     */
    public boolean deleteWallet(Integer id) {
        Wallet wallet = walletRepository.findById(id).orElse(null);

        if (wallet == null) {
            return false;
        } else {
            walletRepository.delete(getWalletById(id));
            return true;
        }
    }
}
