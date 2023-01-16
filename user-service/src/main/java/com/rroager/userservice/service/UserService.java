package com.rroager.userservice.service;

import com.rroager.userservice.entity.User;
import com.rroager.userservice.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getById (long id) {
        logger.info("(getById) Getting user with ID: " + id);

        return userRepository.findById(id).orElse(null);
    }
}
