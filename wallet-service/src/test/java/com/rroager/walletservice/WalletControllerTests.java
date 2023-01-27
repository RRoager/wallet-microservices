package com.rroager.walletservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rroager.walletservice.entity.Wallet;
import com.rroager.walletservice.request.TransactionRequest;
import com.rroager.walletservice.service.WalletService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class WalletControllerTests {

    @Autowired
    private MockMvc mvc;
    @MockBean
    private WalletService walletService;

    @Test
    public void getWalletByIdTest() throws Exception {
        Wallet testWallet = new Wallet(1, 1, 5000.0);

        when(walletService.getWalletById(testWallet.getId())).thenReturn(testWallet);

        mvc.perform(MockMvcRequestBuilders
                        .get("/api/wallet/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance").value(5000));
    }

    @Test
    public void createWalletTest() throws Exception {
        Wallet testWallet = new Wallet(1, 1, 5000.0);

        when(walletService.createWallet(testWallet.getUserId())).thenReturn(testWallet);

        mvc.perform(MockMvcRequestBuilders
                        .post("/api/wallet/user/1/create-wallet")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
    }

    @Test
    public void updateWalletBalanceTest_Success() throws Exception {
        TransactionRequest testTransactionRequest = new TransactionRequest(1, 5000.0, TransactionRequest.TransactionType.DEPOSIT);
        Wallet testWallet = new Wallet(1, 1, 10000.0);

        when(walletService.updateWalletBalance(testTransactionRequest, testWallet)).thenReturn(testWallet);
        when(walletService.getWalletById(testTransactionRequest.getWalletId())).thenReturn(testWallet);

        mvc.perform(MockMvcRequestBuilders
                        .put("/api/wallet/update-wallet")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(asJsonString(testTransactionRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance").value(10000));
    }

    @Test
    public void updateWalletBalanceTest_InsufficientFunds() throws Exception {
        TransactionRequest testTransactionRequest = new TransactionRequest(1, 1000000.0, TransactionRequest.TransactionType.WITHDRAW);
        Wallet testWallet = new Wallet(1, 1, 10000.0);

        when(walletService.getWalletById(testTransactionRequest.getWalletId())).thenReturn(testWallet);
        when(walletService.updateWalletBalance(testTransactionRequest, testWallet)).thenReturn(null);


        mvc.perform(MockMvcRequestBuilders
                        .put("/api/wallet/update-wallet")
                        .content(asJsonString(testTransactionRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Insufficient funds in wallet."));
    }

    @Test
    public void deleteWalletTest_Success() throws Exception {
        Wallet testWallet = new Wallet(1, 1, 10000.0);

        when(walletService.deleteWallet(testWallet.getId())).thenReturn(true);
        when(walletService.getWalletById(testWallet.getId())).thenReturn(testWallet);

        mvc.perform(MockMvcRequestBuilders
                        .delete("/api/wallet/1/delete-wallet")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Deleted wallet with ID: " + testWallet.getId()));
    }

    @Test
    public void deleteWalletTest_Fail() throws Exception {
        when(walletService.deleteWallet(99)).thenReturn(false);
        when(walletService.getWalletById(99)).thenReturn(null);

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
