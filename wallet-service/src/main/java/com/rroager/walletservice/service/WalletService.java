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

    public Wallet getWalletById(String id) {
        logger.info("(getWalletById) Getting wallet with ID: " + id);

        return walletRepository.findById(id).orElse(null);
    }
}
