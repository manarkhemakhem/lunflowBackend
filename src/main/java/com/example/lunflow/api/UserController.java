package com.example.lunflow.api;

import com.example.lunflow.Service.UserService;
import com.example.lunflow.dao.Model.Group;
import com.example.lunflow.dao.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")// Pour Angular
@RestController
@RequestMapping("/api/users")
public class UserController {
    public final UserService userService;
    @Autowired
    public  UserController(UserService userService)
    {
        this.userService=userService;
    }
    @GetMapping("/all")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
    @GetMapping("/{id}")
    public User getUserById(@PathVariable String id) {
        return userService.getUserById(id);
    }
    @GetMapping("/confirmed")
    public List<User> getAllConfirmedTrue() {
        return userService.getAllConfirmedTrue();
    }

    @GetMapping("/NotConfirmed")
    public List<User> getAllConfirmedFalse() {
        return userService.getAllConfirmedFalse();
    }
    @GetMapping("/blocked")
    public List<User> getAllBlockedTrue() {
        return userService.getAllBlockedTrue();
    }

    @GetMapping("/notBlocked")
    public List<User> getAllBlockedFalse() {
        return userService.getAllBlockedFalse();
    }

    @GetMapping("/administrator")
    public List<User> getAllAdminTrue() {
        return userService.getAllAdminTrue();
    }

    @GetMapping("/notadministrator")
    public List<User> getAllAdminFalse() {
        return userService.getAllAdminFalse();
    }
    @GetMapping("/dateCreation")
    public  List<LocalDateTime>  getDateCration() {
        return userService.getAllcreationDate();
    }


}
