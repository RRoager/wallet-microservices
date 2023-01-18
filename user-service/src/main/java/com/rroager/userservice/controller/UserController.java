package com.rroager.userservice.controller;

import com.rroager.userservice.entity.User;
import com.rroager.userservice.response.WalletResponse;
import com.rroager.userservice.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping("/{id}/wallet")
    public WalletResponse getWalletForUser(@PathVariable Long id) {
        return userService.getWalletForUser(userService.getUserById(id).getWalletId());
    }

    @PostMapping("/create-user")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        User newUser = userService.createUser(user);

        if (newUser == null) {
            return new ResponseEntity<>("Email is already taken. Please choose another email.", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("User created successfully", HttpStatus.OK);
    }
}
