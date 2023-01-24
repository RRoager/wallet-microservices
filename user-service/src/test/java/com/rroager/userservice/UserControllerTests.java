package com.rroager.userservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rroager.userservice.entity.User;
import com.rroager.userservice.feign.FeignClient;
import com.rroager.userservice.response.WalletResponse;
import com.rroager.userservice.service.UserService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.sql.Date;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerTests {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private UserService userService;
    @MockBean
    private FeignClient feignClient;

    @Test
    @Order(1)
    public void getUserByIdTest() throws Exception {
        User testUser = new User(1, 1,"Rasmus", "Roager", "rr@test.com", "Test2023!", Date.valueOf("1987-05-10"), "12345678", "2400", "København", "Testvej 12", "Denmark");

        when(userService.getUserById(testUser.getId())).thenReturn(testUser);

        mvc.perform(MockMvcRequestBuilders
                        .get("/api/user/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.walletId").value(1))
                .andExpect(jsonPath("$.firstName").value("Rasmus"))
                .andExpect(jsonPath("$.lastName").value("Roager"))
                .andExpect(jsonPath("$.email").value("rr@test.com"))
                .andExpect(jsonPath("$.dateOfBirth").value("1987-05-10"))
                .andExpect(jsonPath("$.phoneNumber").value("12345678"))
                .andExpect(jsonPath("$.zipCode").value("2400"))
                .andExpect(jsonPath("$.city").value("København"))
                .andExpect(jsonPath("$.address").value("Testvej 12"))
                .andExpect(jsonPath("$.country").value("Denmark"));
    }

    @Test
    @Order(2)
    public void getWalletForUserTest() throws Exception {
        User testUser = new User(1, 1,"Rasmus", "Roager", "rr@test.com", "Test2023!", Date.valueOf("1987-05-10"), "12345678", "2400", "København", "Testvej 12", "Denmark");
        WalletResponse testWalletResponse = new WalletResponse(1, 1, 5000.0);

        when(userService.getUserById(testUser.getId())).thenReturn(testUser);
        when(userService.getWalletForUser(testUser.getWalletId())).thenReturn(testWalletResponse);
        when(feignClient.getWalletById(testUser.getWalletId())).thenReturn(testWalletResponse);

        mvc.perform(MockMvcRequestBuilders
                        .get("/api/user/1/wallet")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.userId").value("1"))
                .andExpect(jsonPath("$.balance").value(5000));
    }

    @Test
    @Order(3)
    public void createUserTest_success() throws Exception {
        User testUser = new User(1, 1,"Rasmus", "Roager", "rr@test.com", "Test2023!", Date.valueOf("1987-05-10"), "12345678", "2400", "København", "Testvej 12", "Denmark");
        WalletResponse testWalletResponse = new WalletResponse(1, 1, 5000.0);

        when(userService.passwordIsValid(testUser.getPassword())).thenReturn(true);
        when(feignClient.createWallet(testUser.getId())).thenReturn(testWalletResponse);
        when(userService.createUser(testUser)).thenReturn(testUser);
        when(userService.getUserById(testUser.getId())).thenReturn(testUser);

        mvc.perform(MockMvcRequestBuilders
                        .post("/api/user/create-user")
                        .content(asJsonString(testUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

//        mvc.perform(MockMvcRequestBuilders
//                        .post("/api/user/create-user")
//                        .content(asJsonString(testUser))
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.walletId").value(1))
//                .andExpect(jsonPath("$.firstName").value("Rasmus"))
//                .andExpect(jsonPath("$.lastName").value("Roager"))
//                .andExpect(jsonPath("$.email").value("rr@test.com"))
//                .andExpect(jsonPath("$.dateOfBirth").value("1987-05-10"))
//                .andExpect(jsonPath("$.phoneNumber").value("12345678"))
//                .andExpect(jsonPath("$.zipCode").value("2400"))
//                .andExpect(jsonPath("$.city").value("København"))
//                .andExpect(jsonPath("$.address").value("Testvej 12"))
//                .andExpect(jsonPath("$.country").value("Denmark"));
    }

    @Test
    @Order(4)
    public void createUserTest_emailExists() throws Exception {
        User testUser = new User(1, 1,"Rasmus", "Roager", "rr@test.com", "Test2023!", Date.valueOf("1987-05-10"), "12345678", "2400", "København", "Testvej 12", "Denmark");

        when(userService.userWithEmailExists(testUser.getEmail())).thenReturn(true);

        mvc.perform(MockMvcRequestBuilders
                        .post("/api/user/create-user")
                        .content(asJsonString(testUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @Order(5)
    public void createUserTest_invalidPassword() throws Exception {
        User testUser = new User(1, 1,"Rasmus", "Roager", "rr@test.com", "test", Date.valueOf("1987-05-10"), "12345678", "2400", "København", "Testvej 12", "Denmark");

        when(userService.passwordIsValid(testUser.getPassword())).thenReturn(false);

        mvc.perform(MockMvcRequestBuilders
                        .post("/api/user/create-user")
                        .content(asJsonString(testUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    // TODO Find out why updateUser returns null
//    @Test
//    @Order(6)
//    public void updateUserTest_success() throws Exception {
//        User testUser = new User(1, 1,"Rasmus", "Roager", "rr@test.com", "Test2023!", Date.valueOf("1987-05-10"), "12345678", "2400", "København", "Testvej 12", "Denmark");
//        User updatedTestUser = new User(1, 1,"UpdatedRasmus", "UpdatedRoager", "updateduser@test.com", "UpdatedTest2023!", Date.valueOf("1987-05-10"), "87654321", "2400", "København", "Testvej 12", "Denmark");
//
//        when(userService.getUserById(testUser.getId())).thenReturn(testUser);
//        when(userService.passwordIsValid(updatedTestUser.getPassword())).thenReturn(true);
//        when(userService.updateUser(testUser.getId(), updatedTestUser)).thenReturn(updatedTestUser);
//
//        mvc.perform(MockMvcRequestBuilders
//                        .put("/api/user/1/update-user")
//                        .content(asJsonString(updatedTestUser))
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.walletId").value(1))
//                .andExpect(jsonPath("$.firstName").value("UpdatedRasmus"))
//                .andExpect(jsonPath("$.lastName").value("UpdatedRoager"))
//                .andExpect(jsonPath("$.email").value("updateduser@test.com"))
//                .andExpect(jsonPath("$.dateOfBirth").value("1987-05-10"))
//                .andExpect(jsonPath("$.phoneNumber").value("87654321"))
//                .andExpect(jsonPath("$.zipCode").value("2400"))
//                .andExpect(jsonPath("$.city").value("København"))
//                .andExpect(jsonPath("$.address").value("Testvej 12"))
//                .andExpect(jsonPath("$.country").value("Denmark"));
//    }

    @Test
    @Order(7)
    public void updateUserTest_emailExists() throws Exception {
        User testUser = new User("updatedTest", "updatedTestesen", "bjarne@hotmail.com", "UpdatedTest2023!", Date.valueOf("1957-05-10"), "12345678", "2400", "København", "Testvej 12", "Denmark");

        mvc.perform(MockMvcRequestBuilders
                        .post("/api/user/1/update-user")
                        .content(asJsonString(testUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @Order(8)
    public void updateUserTest_invalidPassword() throws Exception {
        User testUser = new User("updatedTest", "updatedTestesen", "updatedTest@test.com", "UpdatedTest", Date.valueOf("1957-05-10"), "12345678", "2400", "København", "Testvej 12", "Denmark");

        mvc.perform(MockMvcRequestBuilders
                        .post("/api/user/1/update-user")
                        .content(asJsonString(testUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @Order(9)
    public void deleteUserTest_success() throws Exception {
            mvc.perform(MockMvcRequestBuilders
                            .delete("/api/user/1/delete-user")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent());
    }

    @Test
    @Order(10)
    public void deleteUserTest_userDoesNotExist() throws Exception {
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
