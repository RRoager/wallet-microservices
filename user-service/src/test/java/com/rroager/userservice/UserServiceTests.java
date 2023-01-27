package com.rroager.userservice;

import com.rroager.userservice.entity.User;
import com.rroager.userservice.feign.FeignClient;
import com.rroager.userservice.repository.UserRepository;
import com.rroager.userservice.response.WalletResponse;
import com.rroager.userservice.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTests {
    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private FeignClient feignClient;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    public void getUserByIdTest_success() {
        User testUser = new User(1, 1,"Rasmus", "Roager", "rr@test.com", "Test2023!", Date.valueOf("1987-05-10"), "12345678", "2400", "København", "Testvej 12", "Denmark");

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(testUser));

        assertEquals(testUser, userService.getUserById(1));
    }

    @Test
    public void getUserByIdTest_fail() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertNull(userService.getUserById(1));
    }

    @Test
    public void getWalletForUserTest_success() {
        WalletResponse testWalletResponse = new WalletResponse(1, 1, 5000.0);

        when(feignClient.getWalletById(anyInt())).thenReturn(testWalletResponse);

        assertEquals(testWalletResponse, userService.getWalletForUser(1));
    }

    @Test
    public void getWalletForUserTest_fail() {
        when(feignClient.getWalletById(anyInt())).thenReturn(null);

        assertNull(userService.getWalletForUser(1));
    }

    @Test
    public void createUserTest() {
        User testUser = new User(1, 1,"Rasmus", "Roager", "rr@test.com", "Test2023!", Date.valueOf("1987-05-10"), "12345678", "2400", "København", "Testvej 12", "Denmark");
        WalletResponse testWalletResponse = new WalletResponse(1, 1, 5000.0);

        when(userRepository.save(testUser)).thenReturn(testUser);
        when(feignClient.createWallet(testUser.getId())).thenReturn(testWalletResponse);

        assertEquals(testUser, userService.createUser(testUser));
    }

    @Test
    public void updatedUserTest() {
        User beforeChangeTestUser = new User(1, 1,"Rasmus", "Roager", "rr@test.com", "Test2023!", Date.valueOf("1987-05-10"), "12345678", "2400", "København", "Testvej 12", "Denmark");
        User afterChangeTestUser = new User("updatedRasmus", "updatedRoager", "updatedrr@test.com", "updatedTest2023!", Date.valueOf("1987-05-10"), "87654321", "2400", "updatedKøbenhavn", "updatedTestvej 12", "updatedDenmark");

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(beforeChangeTestUser));
        when(userRepository.save(afterChangeTestUser)).thenReturn(afterChangeTestUser);

        assertEquals(afterChangeTestUser.getFirstName(), userService.updateUser(1, afterChangeTestUser, beforeChangeTestUser).getFirstName());
        assertEquals(afterChangeTestUser.getLastName(), userService.updateUser(1, afterChangeTestUser, beforeChangeTestUser).getLastName());
        assertEquals(afterChangeTestUser.getEmail(), userService.updateUser(1, afterChangeTestUser, beforeChangeTestUser).getEmail());
        assertEquals(afterChangeTestUser.getDateOfBirth(), userService.updateUser(1, afterChangeTestUser, beforeChangeTestUser).getDateOfBirth());
        assertEquals(afterChangeTestUser.getPhoneNumber(), userService.updateUser(1, afterChangeTestUser, beforeChangeTestUser).getPhoneNumber());
        assertEquals(afterChangeTestUser.getZipCode(), userService.updateUser(1, afterChangeTestUser, beforeChangeTestUser).getZipCode());
        assertEquals(afterChangeTestUser.getCity(), userService.updateUser(1, afterChangeTestUser, beforeChangeTestUser).getCity());
        assertEquals(afterChangeTestUser.getAddress(), userService.updateUser(1, afterChangeTestUser, beforeChangeTestUser).getAddress());
        assertEquals(afterChangeTestUser.getCountry(), userService.updateUser(1, afterChangeTestUser, beforeChangeTestUser).getCountry());
    }

    @Test
    public void deleteUserTest() {
        User testUser = new User(1, 1,"Rasmus", "Roager", "rr@test.com", "Test2023!", Date.valueOf("1987-05-10"), "12345678", "2400", "København", "Testvej 12", "Denmark");

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(testUser));
        when(feignClient.deleteWallet(testUser.getWalletId())).thenReturn("Deleted wallet with ID: " + testUser.getWalletId());

        assertTrue(userService.deleteUser(testUser.getId()));
    }
}
