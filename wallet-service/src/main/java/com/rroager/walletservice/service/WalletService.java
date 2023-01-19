package com.rroager.walletservice.service;

import com.rroager.walletservice.entity.Wallet;
import com.rroager.walletservice.repository.WalletRepository;
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
    public Wallet createWallet() {
        Wallet newWallet = walletRepository.save(new Wallet());

        logger.info("(createWallet) Creating wallet with ID: " + newWallet.getId());

        return newWallet;
    }
}
