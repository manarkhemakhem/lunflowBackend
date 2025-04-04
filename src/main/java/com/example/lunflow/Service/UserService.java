package com.example.lunflow.Service;

import com.example.lunflow.dao.Model.Group;
import com.example.lunflow.dao.Model.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    User getUserById(String id);

}
