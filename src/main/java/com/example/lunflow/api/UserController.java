package com.example.lunflow.api;

import com.example.lunflow.Service.UserService;
import com.example.lunflow.dao.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*") // Pour Angular
@RestController
@RequestMapping("/api/{databaseName}/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
    public List<User> getAllUsers(@PathVariable String databaseName) {
        return userService.getAllUsers(databaseName);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable String databaseName, @PathVariable String id) {
        return userService.getUserById(databaseName, id);
    }

    @GetMapping("/confirmed")
    public List<User> getAllConfirmedTrue(@PathVariable String databaseName) {
        return userService.getAllConfirmedTrue(databaseName);
    }

    @GetMapping("/NotConfirmed")
    public List<User> getAllConfirmedFalse(@PathVariable String databaseName) {
        return userService.getAllConfirmedFalse(databaseName);
    }

    @GetMapping("/blocked")
    public List<User> getAllBlockedTrue(@PathVariable String databaseName) {
        return userService.getAllBlockedTrue(databaseName);
    }

    @GetMapping("/notBlocked")
    public List<User> getAllBlockedFalse(@PathVariable String databaseName) {
        return userService.getAllBlockedFalse(databaseName);
    }

    @GetMapping("/administrator")
    public List<User> getAllAdminTrue(@PathVariable String databaseName) {
        return userService.getAllAdminTrue(databaseName);
    }

    @GetMapping("/notadministrator")
    public List<User> getAllAdminFalse(@PathVariable String databaseName) {
        return userService.getAllAdminFalse(databaseName);
    }

    @GetMapping("/dateCreation")
    public List<String> getDateCreation(@PathVariable String databaseName) {
        return userService.getAllCreationDate(databaseName);
    }
}