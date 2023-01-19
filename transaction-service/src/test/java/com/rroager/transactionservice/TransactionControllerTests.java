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

import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TransactionControllerTests {

    @Autowired
    private  MockMvc mvc;

    @Test
    public void getTransactionByIdAndWalletIdTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .get("/api/transaction/f753f2cc-1e67-4ad0-b618-ed9450badf0e/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(1500));
    }

    @Test
    public void getAllTransactionsByWalletIdTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .get("/api/transaction/f753f2cc-1e67-4ad0-b618-ed9450badf0e")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1));
    }

    @Test
    public void getAllByWalletIdFromDateToDateTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .get("/api/transaction/f753f2cc-1e67-4ad0-b618-ed9450badf0e/from/2023-01-03/to/2023-01-04")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(2));
    }

    @Test
    public void createTransactionTest_Success() throws Exception {
        Transaction testTransaction = new Transaction(UUID.fromString("f753f2cc-1e67-4ad0-b618-ed9450badf0e"), 5000.0, TransactionType.DEPOSIT);

        mvc.perform(MockMvcRequestBuilders
                        .post("/api/transaction/f753f2cc-1e67-4ad0-b618-ed9450badf0e/create-transaction")
                        .content(asJsonString(testTransaction))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }


    // Used to send objects as JSON strings
    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
