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
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }

    @GetMapping("/{id}/wallet")
    public ResponseEntity<WalletResponse> getWalletForUser(@PathVariable Integer id) {
        return new ResponseEntity<>(userService.getWalletForUser(userService.getUserById(id).getWalletId()), HttpStatus.OK);
    }

    @PostMapping("/create-user")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        if(userService.userWithEmailExists(user.getEmail())) {
            return new ResponseEntity<>("Email is already taken. Please choose another email.", HttpStatus.BAD_REQUEST);
        }

        if (userService.passwordIsValid(user.getPassword())) {
            return new ResponseEntity<>(userService.createUser(user), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Invalid password. Password must be at least 6 characters long and include one uppercase, one lowercase, one special character and one numeric value", HttpStatus.BAD_REQUEST);
        }
    }

    // TODO make it possible to update users password and delete user
}
