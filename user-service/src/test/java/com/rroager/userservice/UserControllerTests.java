package com.rroager.userservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rroager.userservice.entity.User;
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

import java.sql.Date;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerTests {

    @Autowired
    private MockMvc mvc;

    @Test
    @Order(1)
    public void getUserByIdTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .get("/api/user/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Rasmus"));
    }

    @Test
    @Order(2)
    public void getWalletForUserTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .get("/api/user/1/wallet")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("1"));
    }

    @Test
    @Order(3)
    public void createUserTest_Success() throws Exception {
        User testUser = new User("Test", "Testesen", "test@test.com", "Test2023!", Date.valueOf("1957-05-10"), "12345678", "2400", "København", "Testvej 12", "Denmark");

        mvc.perform(MockMvcRequestBuilders
                        .post("/api/user/create-user")
                        .content(asJsonString(testUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(4)
    public void createUserTest_EmailExists() throws Exception {
        User testUser = new User("Test", "Testesen", "rasmus-roager@hotmail.com", "Test2023!", Date.valueOf("1957-05-10"), "12345678", "2400", "København", "Testvej 12", "Denmark");

        mvc.perform(MockMvcRequestBuilders
                        .post("/api/user/create-user")
                        .content(asJsonString(testUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @Order(5)
    public void createUserTest_InvalidPassword() throws Exception {
        User testUser = new User("Test", "Testesen", "test@test.com", "test", Date.valueOf("1957-05-10"), "12345678", "2400", "København", "Testvej 12", "Denmark");

        mvc.perform(MockMvcRequestBuilders
                        .post("/api/user/create-user")
                        .content(asJsonString(testUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @Order(6)
    public void updateUserTest_Success() throws Exception {
        User testUser = new User("updatedTest", "updatedTestesen", "updatedTest@test.com", "UpdatedTest2023!", Date.valueOf("1957-05-10"), "12345678", "2400", "København", "Testvej 12", "Denmark");

        mvc.perform(MockMvcRequestBuilders
                        .put("/api/user/1/update-user")
                        .content(asJsonString(testUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(7)
    public void updateUserTest_EmailExists() throws Exception {
        User testUser = new User("updatedTest", "updatedTestesen", "bjarne@hotmail.com", "UpdatedTest2023!", Date.valueOf("1957-05-10"), "12345678", "2400", "København", "Testvej 12", "Denmark");

        mvc.perform(MockMvcRequestBuilders
                        .post("/api/user/1/update-user")
                        .content(asJsonString(testUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @Order(8)
    public void updateUserTest_InvalidPassword() throws Exception {
        User testUser = new User("updatedTest", "updatedTestesen", "updatedTest@test.com", "UpdatedTest", Date.valueOf("1957-05-10"), "12345678", "2400", "København", "Testvej 12", "Denmark");

        mvc.perform(MockMvcRequestBuilders
                        .post("/api/user/1/update-user")
                        .content(asJsonString(testUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @Order(9)
    public void deleteUserTest_Success() throws Exception {
            mvc.perform(MockMvcRequestBuilders
                            .delete("/api/user/1/delete-user")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent());
    }

    @Test
    @Order(10)
    public void deleteUserTest_Fail() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .delete("/api/user/99/delete-user")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Not able to delete user. No user with ID: 99"));
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
