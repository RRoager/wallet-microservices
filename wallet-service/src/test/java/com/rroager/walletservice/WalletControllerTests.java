package com.rroager.walletservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rroager.walletservice.request.TransactionRequest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
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
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class WalletControllerTests {

    @Autowired
    private MockMvc mvc;

    @Test
    @Order(1)
    public void getWalletByIdTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .get("/api/wallet/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
    }

    @Test
    @Order(2)
    public void createWalletTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .post("/api/wallet/user/1/create-wallet")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json"));
    }

    @Test
    @Order(3)
    public void updateWalletBalanceTest_Success() throws Exception {
        TransactionRequest testTransactionRequest = new TransactionRequest(1, 5000.0, TransactionRequest.TransactionType.DEPOSIT);

        mvc.perform(MockMvcRequestBuilders
                        .put("/api/wallet/update-wallet")
                        .content(asJsonString(testTransactionRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance").value(10000.0));
    }

    @Test
    @Order(4)
    public void updateWalletBalanceTest_InsufficientFunds() throws Exception {
        TransactionRequest testTransactionRequest = new TransactionRequest(1, 1000000.0, TransactionRequest.TransactionType.WITHDRAW);

        mvc.perform(MockMvcRequestBuilders
                        .put("/api/wallet/update-wallet")
                        .content(asJsonString(testTransactionRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Insufficient funds in wallet."));
    }

    @Test
    @Order(5)
    public void deleteWalletTest_Success() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .delete("/api/wallet/1/delete-wallet")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @Order(6)
    public void deleteWalletTest_Fail() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .delete("/api/wallet/99/delete-wallet")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Not able to delete wallet. No wallet with ID: 99"));
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
