package com.rroager.userservice.service;

import com.rroager.userservice.entity.User;
import com.rroager.userservice.feign.FeignClient;
import com.rroager.userservice.repository.UserRepository;
import com.rroager.userservice.response.WalletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {
    Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final FeignClient feignClient;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, FeignClient feignClient, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.feignClient = feignClient;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     *
     * @param id (Long)
     * @return User
     * Retrieves user based on users id
     * If no user exists with the id, returns null
     */
    public User getUserById(Long id) {
        logger.info("(getUserById) Getting user with ID: " + id);

        return userRepository.findById(id).orElse(null);
    }

    /**
     *
     * @param walletId (UUID)
     * @return WalletResponse
     * Retrieves users wallet from WalletService
     */
    public WalletResponse getWalletForUser(UUID walletId) {
        logger.info("(getWalletForUser) Getting wallet with ID: " + walletId);

        return feignClient.getWalletById(walletId);
    }

    /**
     *
     * @param user (User)
     * @return User
     * Checks if email is already in db
     * Call WalletService to create new wallet and retrieves new walletId
     * Creates new user object with the walletId and details given
     * Saves new user to db
     */
    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            return null;
        }

        logger.info("(createUser) Creating user with email: " + user.getEmail());

        UUID walletId = feignClient.createWallet();
        User newUser = new User(
                walletId,
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                passwordEncoder.encode(user.getPassword()),
                user.getDateOfBirth(),
                user.getPhoneNumber(),
                user.getZipCode(),
                user.getCity(),
                user.getAddress(),
                user.getCountry());
        userRepository.save(newUser);

        return newUser;
    }
}
