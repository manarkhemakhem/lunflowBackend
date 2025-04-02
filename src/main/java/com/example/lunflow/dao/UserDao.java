package com.example.lunflow.dao;

import com.example.lunflow.dao.Model.Group;
import com.example.lunflow.dao.Model.User;

import java.util.List;

public interface UserDao {
    List<User> findAll();
    User findById(String id);
}
