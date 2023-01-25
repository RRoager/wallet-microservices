package com.rroager.userservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rroager.userservice.entity.User;
import com.rroager.userservice.feign.FeignClient;
import com.rroager.userservice.repository.UserRepository;
import com.rroager.userservice.response.WalletResponse;
import com.rroager.userservice.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.sql.Date;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTests {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private UserService userService;
    @MockBean
    private FeignClient feignClient;
    @MockBean
    private UserRepository userRepository;

    @Test
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
    public void createUserTest_invalidPassword() throws Exception {
        User testUser = new User(1, 1,"Rasmus", "Roager", "rr@test.com", "test", Date.valueOf("1987-05-10"), "12345678", "2400", "København", "Testvej 12", "Denmark");

        when(userService.passwordIsValid(testUser.getPassword())).thenReturn(false);

        mvc.perform(MockMvcRequestBuilders
                        .post("/api/user/create-user")
                        .content(asJsonString(testUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void updateUserTest_success() throws Exception {
        User testUser = new User(1, 1,"Rasmus", "Roager", "rr@test.com", "Test2023!", Date.valueOf("1987-05-10"), "12345678", "2400", "København", "Testvej 12", "Denmark");
        User updatedTestUser = new User(1, 1,"UpdatedRasmus", "UpdatedRoager", "updateduser@test.com", "UpdatedTest2023!", Date.valueOf("1987-05-10"), "87654321", "2400", "København", "Testvej 12", "Denmark");

        when(userService.userWithEmailExists(updatedTestUser.getEmail())).thenReturn(false);
        when(userService.passwordIsValid(updatedTestUser.getPassword())).thenReturn(true);
        when(userService.updateUser(testUser.getId(), updatedTestUser)).thenReturn(updatedTestUser);
        when(userService.getUserById(testUser.getId())).thenReturn(testUser);

        // Are these necessary when I already mock the updateUser and getUserById calls?
        when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
        when(userRepository.save(updatedTestUser)).thenReturn(updatedTestUser);

        mvc.perform(MockMvcRequestBuilders
                        .put("/api/user/1/update-user")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedTestUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.walletId").value(1))
                .andExpect(jsonPath("$.firstName").value("UpdatedRasmus"))
                .andExpect(jsonPath("$.lastName").value("UpdatedRoager"))
                .andExpect(jsonPath("$.email").value("updateduser@test.com"))
                .andExpect(jsonPath("$.dateOfBirth").value("1987-05-10"))
                .andExpect(jsonPath("$.phoneNumber").value("87654321"))
                .andExpect(jsonPath("$.zipCode").value("2400"))
                .andExpect(jsonPath("$.city").value("København"))
                .andExpect(jsonPath("$.address").value("Testvej 12"))
                .andExpect(jsonPath("$.country").value("Denmark"));
    }

    @Test
    public void updateUserTest_emailExists() throws Exception {
        User updatedTestUser = new User(1, 1,"UpdatedRasmus", "UpdatedRoager", "updateduser@test.com", "UpdatedTest2023!", Date.valueOf("1987-05-10"), "87654321", "2400", "København", "Testvej 12", "Denmark");

        when(userService.userWithEmailExists(updatedTestUser.getEmail())).thenReturn(true);

        mvc.perform(MockMvcRequestBuilders
                        .post("/api/user/1/update-user")
                        .content(asJsonString(updatedTestUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void updateUserTest_invalidPassword() throws Exception {
        User updatedTestUser = new User(1, 1,"UpdatedRasmus", "UpdatedRoager", "updateduser@test.com", "UpdatedTest2023!", Date.valueOf("1987-05-10"), "87654321", "2400", "København", "Testvej 12", "Denmark");

        when(userService.userWithEmailExists(updatedTestUser.getEmail())).thenReturn(false);
        when(userService.passwordIsValid(updatedTestUser.getPassword())).thenReturn(false);

        mvc.perform(MockMvcRequestBuilders
                        .post("/api/user/1/update-user")
                        .content(asJsonString(updatedTestUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void deleteUserTest_success() throws Exception {
        User testUser = new User(1, 1,"Rasmus", "Roager", "rr@test.com", "Test2023!", Date.valueOf("1987-05-10"), "12345678", "2400", "København", "Testvej 12", "Denmark");

        when(userService.deleteUser(testUser.getId())).thenReturn(true);
        when(userService.getUserById(testUser.getId())).thenReturn(testUser);
        when(feignClient.deleteWallet(testUser.getWalletId())).thenReturn("Deleted wallet with ID: " + testUser.getWalletId());

        mvc.perform(MockMvcRequestBuilders
                        .delete("/api/user/1/delete-user")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Deleted user with ID: " + testUser.getId()));
    }

    @Test
    public void deleteUserTest_userDoesNotExist() throws Exception {
        when(userService.deleteUser(99)).thenReturn(false);
        when(userService.getUserById(99)).thenReturn(null);

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
