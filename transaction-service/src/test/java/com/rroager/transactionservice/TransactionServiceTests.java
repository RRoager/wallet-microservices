package com.rroager.transactionservice;

import com.rroager.transactionservice.entity.Transaction;
import com.rroager.transactionservice.feign.FeignClient;
import com.rroager.transactionservice.repository.TransactionRepository;
import com.rroager.transactionservice.response.WalletResponse;
import com.rroager.transactionservice.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.rroager.transactionservice.entity.TransactionType.DEPOSIT;
import static com.rroager.transactionservice.entity.TransactionType.WITHDRAW;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TransactionServiceTests {
    @InjectMocks
    private TransactionService transactionService;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private FeignClient feignClient;

    @Test
    public void getTransactionByIdAndWalletIdTest() {
        Transaction testTransaction = new Transaction(1, 1, 1500.0, 5500.0, Date.valueOf("2023-01-01"), DEPOSIT);

        when(transactionRepository.findByIdAndWalletId(anyInt(), anyInt())).thenReturn(Optional.of(testTransaction));

        assertEquals(testTransaction, transactionService.getTransactionByIdAndWalletId(1, 1));
    }

    @Test
    public void getAllTransactionsWalletIdTest() {
        Transaction testTransaction1 = new Transaction(1, 1, 1500.0, 5500.0, Date.valueOf("2023-01-01"), DEPOSIT);
        Transaction testTransaction2 = new Transaction(1, 1, 2500.0, 3000.0, Date.valueOf("2023-01-03"), WITHDRAW);
        List<Transaction> testTransactionList = Arrays.asList(testTransaction1, testTransaction2);

        when(transactionRepository.findAllByWalletId(anyInt())).thenReturn(testTransactionList);

        assertEquals(testTransactionList, transactionService.getAllTransactionsWalletId(1));
    }

    @Test
    public void getAllByWalletIdFromDateToDateTest() {
        Transaction testTransaction1 = new Transaction(1, 1, 1500.0, 5500.0, Date.valueOf("2023-01-01"), DEPOSIT);
        Transaction testTransaction2 = new Transaction(1, 1, 2500.0, 3000.0, Date.valueOf("2023-01-03"), WITHDRAW);
        List<Transaction> testTransactionList = Arrays.asList(testTransaction1, testTransaction2);

        when(transactionRepository.findAllByWalletIdFromDateToDate(1, Date.valueOf("2023-01-01"), Date.valueOf("2023-01-03"))).thenReturn(testTransactionList);

        assertEquals(testTransactionList, transactionService.getAllByWalletIdFromDateToDate(1, Date.valueOf("2023-01-01"), Date.valueOf("2023-01-03")));
    }

    @Test
    public void createTransactionTest() {
        WalletResponse testWalletResponse = new WalletResponse(1, 5000.0);
        Transaction testTransaction = new Transaction(1, 5000.0, 5000.0, DEPOSIT);

        when(feignClient.updateWalletBalance(testTransaction)).thenReturn(new ResponseEntity<>(testWalletResponse, HttpStatus.OK));
        when(transactionRepository.save(any())).thenReturn(testTransaction);

        Transaction transactionResult = transactionService.createTransaction(1, testTransaction);

        assertEquals(testTransaction.getId(), transactionResult.getId());
        assertEquals(testTransaction.getWalletId(), transactionResult.getWalletId());
        assertEquals(testTransaction.getAmount(), transactionResult.getAmount());
        assertEquals(testTransaction.getCurrentBalance(), transactionResult.getCurrentBalance());
        assertEquals(testTransaction.getTransactionType(), transactionResult.getTransactionType());
    }
}
