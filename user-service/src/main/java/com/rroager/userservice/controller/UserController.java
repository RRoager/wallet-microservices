package com.rroager.userservice.controller;

import com.rroager.userservice.entity.User;
import com.rroager.userservice.response.WalletResponse;
import com.rroager.userservice.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
