package com.rroager.userservice.service;

import com.rroager.userservice.entity.User;
import com.rroager.userservice.feign.FeignClient;
import com.rroager.userservice.repository.UserRepository;
import com.rroager.userservice.response.WalletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final FeignClient feignClient;

    public UserService(UserRepository userRepository, FeignClient feignClient) {
        this.userRepository = userRepository;
        this.feignClient = feignClient;
    }

    public User getUserById(Long id) {
        logger.info("(getUserById) Getting user with ID: " + id);

        return userRepository.findById(id).orElse(null);
    }

    public WalletResponse getWalletForUser(String walletId) {
        logger.info("(getWalletForUser) Getting wallet with ID: " + walletId);

        return feignClient.getWalletById(walletId);
    }
}
