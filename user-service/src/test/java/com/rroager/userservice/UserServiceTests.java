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

import java.sql.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
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

    // TODO fix return from feign call
    @Test
    public void createUserTest_success() {
        User testUser = new User(1, 1,"Rasmus", "Roager", "rr@test.com", "Test2023!", Date.valueOf("1987-05-10"), "12345678", "2400", "København", "Testvej 12", "Denmark");

        when(userRepository.save(testUser)).thenReturn(testUser);
        when(feignClient.createWallet(testUser.getId()).getId()).thenReturn(testUser.getWalletId());

        assertEquals(testUser, userService.createUser(testUser));
    }
}
