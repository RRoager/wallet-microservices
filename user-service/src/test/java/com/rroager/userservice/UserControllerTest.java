package com.rroager.userservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rroager.userservice.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.sql.Date;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void getUserByIdTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .get("/api/user/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Rasmus"));
    }

    @Test
    public void getWalletForUserTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .get("/api/user/1/wallet")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("f753f2cc-1e67-4ad0-b618-ed9450badf0e"));
    }

    @Test
    public void createUserTest_Success() throws Exception {
        User testUser = new User(UUID.randomUUID(), "Test", "Testesen", "test@test.com", "Test2023!", Date.valueOf("1957-05-10"), "12345678", "2400", "København", "Testvej 12", "Denmark");

        mvc.perform(MockMvcRequestBuilders
                .post("/api/user/create-user")
                .content(asJsonString(testUser))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void createUserTest_EmailExists() throws Exception {
        User testUser = new User(UUID.randomUUID(), "Test", "Testesen", "rasmus-roager@hotmail.com", "Test2023!", Date.valueOf("1957-05-10"), "12345678", "2400", "København", "Testvej 12", "Denmark");

        mvc.perform(MockMvcRequestBuilders
                        .post("/api/user/create-user")
                        .content(asJsonString(testUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void createUserTest_InvalidPassword() throws Exception {
        User testUser = new User(UUID.randomUUID(), "Test", "Testesen", "test@test.com", "test", Date.valueOf("1957-05-10"), "12345678", "2400", "København", "Testvej 12", "Denmark");

        mvc.perform(MockMvcRequestBuilders
                        .post("/api/user/create-user")
                        .content(asJsonString(testUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
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
