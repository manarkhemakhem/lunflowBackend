package com.example.lunflow.dao.impl;

import com.example.lunflow.dao.GroupDao;
import com.example.lunflow.dao.Model.Group;
import com.example.lunflow.dao.Model.User;
import com.example.lunflow.dao.Repository.GroupRepo;
import com.example.lunflow.dao.Repository.UserRepo;
import com.example.lunflow.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository

public class UserDaoImpl implements UserDao {
    private final UserRepo userRepo;
    @Autowired
    public UserDaoImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }


    @Override
    public List<User> findAll() {
        return userRepo.findAll();
    }

    @Override
    public User findById(String id) {
        return userRepo.findById(id).orElse(null);
    }}