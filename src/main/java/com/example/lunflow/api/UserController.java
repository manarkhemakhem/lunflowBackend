package com.example.lunflow.api;

import com.example.lunflow.Service.UserService;
import com.example.lunflow.dao.Model.Group;
import com.example.lunflow.dao.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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


}
