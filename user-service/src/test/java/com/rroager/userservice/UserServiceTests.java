package com.rroager.userservice;

import com.rroager.userservice.entity.User;
import com.rroager.userservice.feign.FeignClient;
import com.rroager.userservice.repository.UserRepository;
import com.rroager.userservice.response.WalletResponse;
import com.rroager.userservice.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.Date;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

    @ParameterizedTest
    @MethodSource("userAndUpdatedUser")
    public void updatedUserTest(User input, User output) {

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(input));
        when(userRepository.save(output)).thenReturn(output);

        assertEquals(output.getFirstName(), userService.updateUser(1, output, input).getFirstName());
        assertEquals(output.getLastName(), userService.updateUser(1, output, input).getLastName());
        assertEquals(output.getEmail(), userService.updateUser(1, output, input).getEmail());
        assertEquals(output.getDateOfBirth(), userService.updateUser(1, output, input).getDateOfBirth());
        assertEquals(output.getPhoneNumber(), userService.updateUser(1, output, input).getPhoneNumber());
        assertEquals(output.getZipCode(), userService.updateUser(1, output, input).getZipCode());
        assertEquals(output.getCity(), userService.updateUser(1, output, input).getCity());
        assertEquals(output.getAddress(), userService.updateUser(1, output, input).getAddress());
        assertEquals(output.getCountry(), userService.updateUser(1, output, input).getCountry());
    }

    private static Stream<Arguments> userAndUpdatedUser() {
        return Stream.of(
                Arguments.of(new User(1, 1,"Rasmus", "Roager", "rr@test.com", "Test2023!", Date.valueOf("1987-05-10"), "12345678", "2400", "København", "Testvej 12", "Denmark"),
                        new User(1, 1,"UpdatedRasmus", "UpdatedRoager", "updateduser@test.com", "UpdatedTest2023!", Date.valueOf("1987-05-10"), "87654321", "2400", "København", "Testvej 12", "Denmark")),
                Arguments.of(new User(1, 1,"Bjarne", "Goldbaek", "bg@test.com", "Bjarne123!", Date.valueOf("1950-01-15"), "12345678", "4000", "Roskilde", "Testvej 12", "Denmark"),
                        new User(1, 1,"UpdatedBjarne", "UpdatedGoldbaek", "updatedbg@test.com", "UpdatedBjarne123!", Date.valueOf("1951-01-15"), "87654321", "2400", "København", "Testvej 12", "Denmark"))
        );
    }

    @Test
    public void deleteUserTest() {
        User testUser = new User(1, 1,"Rasmus", "Roager", "rr@test.com", "Test2023!", Date.valueOf("1987-05-10"), "12345678", "2400", "København", "Testvej 12", "Denmark");

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(testUser));
        when(feignClient.deleteWallet(testUser.getWalletId())).thenReturn("Deleted wallet with ID: " + testUser.getWalletId());

        assertTrue(userService.deleteUser(testUser.getId()));
    }
}
