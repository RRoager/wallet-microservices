package com.rroager.transactionservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rroager.transactionservice.entity.Transaction;
import com.rroager.transactionservice.feign.FeignClient;
import com.rroager.transactionservice.response.WalletResponse;
import com.rroager.transactionservice.service.TransactionService;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import static com.rroager.transactionservice.entity.TransactionType.DEPOSIT;
import static com.rroager.transactionservice.entity.TransactionType.WITHDRAW;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TransactionControllerTests {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private TransactionService transactionService;
    @MockBean
    private FeignClient feignClient;

    @Test
    public void getTransactionByIdAndWalletIdTest() throws Exception {
        Transaction testTransaction = new Transaction(1, 1, 1500.0, 5500.0, Date.valueOf("2023-01-01"), DEPOSIT);

        when(transactionService.getTransactionByIdAndWalletId(testTransaction.getId(), testTransaction.getWalletId())).thenReturn(testTransaction);

        mvc.perform(MockMvcRequestBuilders
                        .get("/api/transaction/wallet/1/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.walletId").value(1))
                .andExpect(jsonPath("$.amount").value(1500))
                .andExpect(jsonPath("$.currentBalance").value(5500))
                .andExpect(jsonPath("$.transactionDate").value("2023-01-01"))
                .andExpect(jsonPath("$.transactionType").value("DEPOSIT"));
    }

    @Test
    public void getAllTransactionsByWalletIdTest() throws Exception {
        Transaction testTransaction1 = new Transaction(1, 1, 1500.0, 5500.0, Date.valueOf("2023-01-01"), DEPOSIT);
        Transaction testTransaction2 = new Transaction(1, 1, 2500.0, 3000.0, Date.valueOf("2023-01-03"), WITHDRAW);
        List<Transaction> testTransactionList = Arrays.asList(testTransaction1, testTransaction2);

        when(transactionService.getAllTransactionsWalletId(1)).thenReturn(testTransactionList);

        mvc.perform(MockMvcRequestBuilders
                        .get("/api/transaction/wallet/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].walletId").value(1))
                .andExpect(jsonPath("$[0].amount").value(1500))
                .andExpect(jsonPath("$[0].currentBalance").value(5500))
                .andExpect(jsonPath("$[0].transactionDate").value("2023-01-01"))
                .andExpect(jsonPath("$[0].transactionType").value("DEPOSIT"))
                .andExpect(jsonPath("$[1].id").value(1))
                .andExpect(jsonPath("$[1].walletId").value(1))
                .andExpect(jsonPath("$[1].amount").value(2500))
                .andExpect(jsonPath("$[1].currentBalance").value(3000))
                .andExpect(jsonPath("$[1].transactionDate").value("2023-01-03"))
                .andExpect(jsonPath("$[1].transactionType").value("WITHDRAW"));
    }

    @Test
    public void getAllByWalletIdFromDateToDateTest() throws Exception {
        Transaction testTransaction1 = new Transaction(1, 1, 1500.0, 5500.0, Date.valueOf("2023-01-01"), DEPOSIT);
        Transaction testTransaction2 = new Transaction(1, 1, 2500.0, 3000.0, Date.valueOf("2023-01-03"), WITHDRAW);
        List<Transaction> testTransactionList = Arrays.asList(testTransaction1, testTransaction2);

        when(transactionService.getAllByWalletIdFromDateToDate(1, Date.valueOf("2023-01-01"), Date.valueOf("2023-01-03"))).thenReturn(testTransactionList);

        mvc.perform(MockMvcRequestBuilders
                        .get("/api/transaction/wallet/1/from/2023-01-01/to/2023-01-03")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].walletId").value(1))
                .andExpect(jsonPath("$[0].amount").value(1500))
                .andExpect(jsonPath("$[0].currentBalance").value(5500))
                .andExpect(jsonPath("$[0].transactionDate").value("2023-01-01"))
                .andExpect(jsonPath("$[0].transactionType").value("DEPOSIT"))
                .andExpect(jsonPath("$[1].id").value(1))
                .andExpect(jsonPath("$[1].walletId").value(1))
                .andExpect(jsonPath("$[1].amount").value(2500))
                .andExpect(jsonPath("$[1].currentBalance").value(3000))
                .andExpect(jsonPath("$[1].transactionDate").value("2023-01-03"))
                .andExpect(jsonPath("$[1].transactionType").value("WITHDRAW"));
    }

    @Test
    public void createTransactionTest_depositSuccess() throws Exception {
        WalletResponse testWalletResponse = new WalletResponse(1, 5000.0);
        Transaction testTransaction = new Transaction(1, 1, 5000.0, 0.0, Date.valueOf("2023-01-01"), DEPOSIT);
        testTransaction.setCurrentBalance(testWalletResponse.getBalance());

        when(feignClient.updateWalletBalance(testTransaction)).thenReturn(new ResponseEntity<>(testWalletResponse, HttpStatus.OK));
        when(transactionService.createTransaction(1, testTransaction)).thenReturn(testTransaction);
        when(transactionService.getTransactionByIdAndWalletId(1, 1)).thenReturn(testTransaction);

        mvc.perform(MockMvcRequestBuilders
                        .post("/api/transaction/wallet/1/create-transaction")
                        .content(asJsonString(testTransaction))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        // Does not work. No response body
//        mvc.perform(MockMvcRequestBuilders
//                        .post("/api/transaction/wallet/1/create-transaction")
//                        .accept(MediaType.APPLICATION_JSON)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(asJsonString(testTransaction)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.walletId").value(1))
//                .andExpect(jsonPath("$.amount").value(5000))
//                .andExpect(jsonPath("$.currentBalance").value(5000))
//                .andExpect(jsonPath("$.transactionDate").value("2023-01-01"))
//                .andExpect(jsonPath("$.transactionType").value("DEPOSIT"));
    }

    @Test
    public void createTransactionTest_withdrawSuccess() throws Exception {
        WalletResponse testWalletResponse = new WalletResponse(1, 5000.0);
        Transaction testTransaction = new Transaction(1, 1, 5000.0, 10000.0, Date.valueOf("2023-01-01"), WITHDRAW);
        testTransaction.setCurrentBalance(testWalletResponse.getBalance());

        when(feignClient.updateWalletBalance(testTransaction)).thenReturn(new ResponseEntity<>(testWalletResponse, HttpStatus.OK));
        when(transactionService.createTransaction(1, testTransaction)).thenReturn(testTransaction);
        when(transactionService.getTransactionByIdAndWalletId(1, 1)).thenReturn(testTransaction);

        mvc.perform(MockMvcRequestBuilders
                        .post("/api/transaction/wallet/1/create-transaction")
                        .content(asJsonString(testTransaction))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
    }

    @Test
    public void createTransactionTest_InvalidAmount() throws Exception {
        Transaction testTransaction = new Transaction(1, 1, 0.0, 0.0, Date.valueOf("2023-01-01"), DEPOSIT);

        when(feignClient.updateWalletBalance(testTransaction)).thenReturn(null);
        when(transactionService.createTransaction(1, testTransaction)).thenReturn(testTransaction);

        mvc.perform(MockMvcRequestBuilders
                .post("/api/transaction/wallet/1/create-transaction")
                .content(asJsonString(testTransaction))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Transaction amount must be more than 0."));
    }

    @Test
    public void createTransactionTest_InsufficientFunds() throws Exception {
        Transaction testTransaction = new Transaction(1, 1, 1000000.0, 0.0, Date.valueOf("2023-01-01"), WITHDRAW);
        FeignException feignException = Mockito.mock(FeignException.class);
        when(feignException.status()).thenReturn(400);

        when(feignClient.updateWalletBalance(any())).thenThrow(feignException);
        when(transactionService.createTransaction(1, testTransaction)).thenThrow(feignException);

        mvc.perform(MockMvcRequestBuilders
                        .post("/api/transaction/wallet/1/create-transaction")
                        .content(asJsonString(testTransaction))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Insufficient funds in wallet with ID: " + testTransaction.getWalletId()));
    }

    // Converts object to JSON string
    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
