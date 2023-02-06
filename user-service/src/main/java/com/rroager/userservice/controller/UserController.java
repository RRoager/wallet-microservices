package com.rroager.userservice.controller;

import com.rroager.userservice.entity.User;
import com.rroager.userservice.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    public ResponseEntity<?> getUserById(@PathVariable Integer id) {
        User user = userService.getUserById(id);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No user with ID: " + id, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}/wallet")
    public ResponseEntity<?> getWalletForUser(@PathVariable Integer id) {
        User user = userService.getUserById(id);
        if (user != null) {
            return new ResponseEntity<>(userService.getWalletForUser(user.getWalletId()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Not able to show wallet. No user with ID: " + id, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/create-user")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        if(userService.userWithEmailExists(user.getEmail())) {
            return new ResponseEntity<>("Email is already taken. Please choose another email.", HttpStatus.BAD_REQUEST);
        }

        if (userService.passwordIsValid(user.getPassword())) {
            return new ResponseEntity<>(userService.createUser(user), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Invalid password. Password must be at least 6 characters long and include one uppercase, one lowercase, one special character and one numeric value.", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}/update-user")
    public ResponseEntity<?> updateUser(@PathVariable Integer id, @RequestBody User updatedUser) {
        User user = userService.getUserById(id);
        if (user == null) {
            return new ResponseEntity<>("Not able to update user. No user with ID: " + id, HttpStatus.NOT_FOUND);
        }

        if (updatedUser.getEmail().equalsIgnoreCase(userService.getUserById(id).getEmail()) || !userService.userWithEmailExists(updatedUser.getEmail())) {
            if (userService.passwordIsValid(updatedUser.getPassword())) {
                return new ResponseEntity<>(userService.updateUser(id, updatedUser, user), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Invalid password. Password must be at least 6 characters long and include one uppercase, one lowercase, one special character and one numeric value.", HttpStatus.BAD_REQUEST);
            }
        } else if (userService.userWithEmailExists(updatedUser.getEmail())) {
            return new ResponseEntity<>("Email is already taken. Please choose another email.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Something went wrong", HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{id}/delete-user")
    public ResponseEntity<String> deleteUser(@PathVariable Integer id) {
        if (userService.deleteUser(id)) {
            return new ResponseEntity<>("Deleted user with ID: " + id, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Not able to delete user. No user with ID: " + id, HttpStatus.NOT_FOUND);
        }
    }

}
