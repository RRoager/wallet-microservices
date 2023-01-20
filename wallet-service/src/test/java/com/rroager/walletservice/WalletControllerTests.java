package com.rroager.walletservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rroager.walletservice.request.TransactionRequest;
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
public class WalletControllerTests {

    @Autowired
    private MockMvc mvc;

    @Test
    public void getWalletByIdTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .get("/api/wallet/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
    }

    @Test
    public void createWalletTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .post("/api/wallet/create-wallet")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json"));
    }

    @Test
    public void updateWalletBalanceTest_Success() throws Exception {
        TransactionRequest testTransactionRequest = new TransactionRequest(1, 5000.0, TransactionRequest.TransactionType.DEPOSIT);

        mvc.perform(MockMvcRequestBuilders
                        .put("/api/wallet/update-wallet")
                        .content(asJsonString(testTransactionRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Updated balance of wallet with ID: 1"));
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
