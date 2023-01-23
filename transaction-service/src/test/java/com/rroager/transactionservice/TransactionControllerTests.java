package com.rroager.transactionservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rroager.transactionservice.entity.Transaction;
import com.rroager.transactionservice.entity.TransactionType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TransactionControllerTests {

    @Autowired
    private MockMvc mvc;

    @Test
    public void getTransactionByIdAndWalletIdTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .get("/api/transaction/wallet/1/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(1500));
    }

    @Test
    public void getAllTransactionsByWalletIdTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .get("/api/transaction/wallet/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1));
    }

    @Test
    public void getAllByWalletIdFromDateToDateTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .get("/api/transaction/wallet/1/from/2023-01-03/to/2023-01-04")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(2));
    }

    // TODO find a way to delete data from WalletService database after tests
    @Test
    public void createTransactionTest_Success() throws Exception {
        Transaction testTransaction = new Transaction(1, 5000.0, 5000.0, TransactionType.DEPOSIT);

        mvc.perform(MockMvcRequestBuilders
                        .post("/api/transaction/wallet/1/create-transaction")
                        .content(asJsonString(testTransaction))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.currentBalance").value(10000.0));
    }

    @Test
    public void createTransactionTest_InvalidAmount() throws Exception {
        Transaction testTransaction = new Transaction(1, 0.0, 5000.0, TransactionType.DEPOSIT);

        mvc.perform(MockMvcRequestBuilders
                        .post("/api/transaction/wallet/1/create-transaction")
                        .content(asJsonString(testTransaction))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Transaction amount must be more than 0."));
    }

    @Test
    public void createTransactionTest_InsufficientFunds() throws Exception {
        Transaction testTransaction = new Transaction(1, 1000000.0, 5000.0, TransactionType.WITHDRAW);

        mvc.perform(MockMvcRequestBuilders
                        .post("/api/transaction/wallet/1/create-transaction")
                        .content(asJsonString(testTransaction))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Insufficient funds in wallet."));
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
