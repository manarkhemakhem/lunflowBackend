package com.example.lunflow.dao;

import com.example.lunflow.dao.Model.Group;
import com.example.lunflow.dao.Model.User;

import java.util.List;

public interface UserDao {
    List<User> findAll();
    User findById(String id);

    List<User> findByConfirmedTrue();
    List<User> findByConfirmedFalse();

    List<User> findByBlockedTrue();
    List<User> findByBlockedFalse();

    List<User> findByAdministratorTrue();
    List<User> findByAdministratorFalse();
}
