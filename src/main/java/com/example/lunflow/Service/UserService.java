package com.example.lunflow.Service;

import com.example.lunflow.dao.Model.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers(String databaseName);
    User getUserById(String databaseName, String id);
    List<User> getAllConfirmedTrue(String databaseName);
    List<User> getAllConfirmedFalse(String databaseName);
    List<User> getAllBlockedTrue(String databaseName);
    List<User> getAllBlockedFalse(String databaseName);
    List<User> getAllAdminTrue(String databaseName);
    List<User> getAllAdminFalse(String databaseName);
    List<String> getAllCreationDate(String databaseName);
}