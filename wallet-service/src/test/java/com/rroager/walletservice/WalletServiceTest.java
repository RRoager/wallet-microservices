package com.rroager.walletservice;

import com.rroager.walletservice.entity.Wallet;
import com.rroager.walletservice.repository.WalletRepository;
import com.rroager.walletservice.request.TransactionRequest;
import com.rroager.walletservice.service.WalletService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@SpringBootTest
public class WalletServiceTest {
    @InjectMocks
    private WalletService walletService;
    @Mock
    private WalletRepository walletRepository;

    @Test
    public void getWalletById_success() {
        Wallet testWallet = new Wallet(1, 1, 5000.0);

        when(walletRepository.findById(anyInt())).thenReturn(Optional.of(testWallet));

        assertEquals(testWallet, walletService.getWalletById(1));
    }

    @Test
    public void getWalletById_fail() {
        when(walletRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertNull(walletService.getWalletById(1));
    }

    @Test
    public void createWalletTest() {
        Wallet testWallet = new Wallet(1, 1, 5000.0);

        when(walletRepository.save(new Wallet(testWallet.getUserId()))).thenReturn(testWallet);

        assertEquals(testWallet, walletService.createWallet(1));
    }

    @Test
    public void updateWalletBalanceTest() {
        Wallet testWallet = new Wallet(1, 1, 5000.0);
        TransactionRequest testTransactionRequest = new TransactionRequest(1, 5000.0, TransactionRequest.TransactionType.DEPOSIT);

        when(walletRepository.findById(anyInt())).thenReturn(Optional.of(testWallet));
        when(walletRepository.save(testWallet)).thenReturn(testWallet);

        assertEquals(testWallet.getBalance() + testTransactionRequest.getAmount(), walletService.updateWalletBalance(testTransactionRequest, testWallet).getBalance());
    }

    @Test
    public void deleteWalletTest() {
        Wallet testWallet = new Wallet(1, 1, 5000.0);

        when(walletRepository.findById(anyInt())).thenReturn(Optional.of(testWallet));

        assertTrue(walletService.deleteWallet(testWallet.getId()));
    }
}