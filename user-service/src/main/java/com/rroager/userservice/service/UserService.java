package com.rroager.userservice.service;

import com.rroager.userservice.entity.User;
import com.rroager.userservice.feign.FeignClient;
import com.rroager.userservice.repository.UserRepository;
import com.rroager.userservice.response.WalletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

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
    public User getUserById(Integer id) {
        logger.info("(getUserById) Getting user with ID: " + id);

        return userRepository.findById(id).orElse(null);
    }

    /**
     *
     * @param walletId (Integer)
     * @return WalletResponse
     * Retrieves users wallet from WalletService
     */
    public WalletResponse getWalletForUser(Integer walletId) {
        logger.info("(getWalletForUser) Getting wallet with ID: " + walletId);

        return feignClient.getWalletById(walletId);
    }

    /**
     *
     * @param user (User)
     * @return User
     * Creates new user object with the newly created walletId and details given
     * Saves new user to db
     * Call WalletService to create new wallet and retrieves new walletId
     * Sets new walletId on new user and saves
     */
    public User createUser(User user) {
        logger.info("(createUser) Creating user with email: " + user.getEmail());

        User newUser = userRepository.save(user);
        setUserData(user, newUser);
        newUser.setWalletId(feignClient.createWallet(newUser.getId()).getId());

        return userRepository.save(newUser);
    }

    /**
     *
     * @param updatedUser (User)
     * @return User
     * Retrieves user based on user ID
     * Updates user with the details given
     * Saves user to db
     * If user is null, no user exists with the ID and null is returned
     */
    public User updateUser(Integer id, User updatedUser, User user) {
        logger.info("(updateUser) Updating user with ID: " + id);

        setUserData(updatedUser, user);
        userRepository.save(user);

        return user;
    }

    /**
     *
     * @param updatedUser (User)
     * @param user (User)
     * Set new user data
     */
    private void setUserData(User updatedUser, User user) {
        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
        user.setEmail(updatedUser.getEmail());
        user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        user.setDateOfBirth(updatedUser.getDateOfBirth());
        user.setPhoneNumber(updatedUser.getPhoneNumber());
        user.setZipCode(updatedUser.getZipCode());
        user.setCity(updatedUser.getCity());
        user.setAddress(updatedUser.getAddress());
        user.setCountry(updatedUser.getCountry());
    }

    /**
     *
     * @param email (String)
     * @return Boolean
     * Checks if email is already stored in db
     */
    public Boolean userWithEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     *
     * @param password (String)
     * @return Boolean
     * Checks that password is at least 6 characters long
     * Creates regex for all required characters (uppercase, lowercase, special character and numeric value)
     * Checks if password matches regex
     */
    public Boolean passwordIsValid(String password) {
        if (password.length() < 6) {
            return false;
        }

        String regex = "^(?=.*?\\p{Lu})(?=.*?\\p{Ll})(?=.*?\\d)" +
                "(?=.*?[`~!@#$%^&*()\\-_=+\\\\|\\[{\\]};:'\",<.>/?]).*$";

        return Pattern.compile(regex).matcher(password).matches();
    }

    /**
     *
     * @param id (Integer)
     * @return boolean
     * Retrieves user based on ID
     * Deletes users wallet and user from db
     * If user is null, no user exists with the ID and null is returned
     */
    public boolean deleteUser(Integer id) {
        User user = getUserById(id);

        if (user == null) {
            return false;
        } else {
            logger.info("(deleteWallet) Deleting wallet with ID: " + id);
            feignClient.deleteWallet(user.getWalletId());
            userRepository.delete(user);
            return true;
        }
    }

}
