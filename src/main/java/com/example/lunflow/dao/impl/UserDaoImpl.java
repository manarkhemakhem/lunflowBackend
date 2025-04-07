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
    }

    @Override
    public List<User> findByConfirmedTrue() {
        return userRepo.findByConfirmedTrue();
    }

    @Override
    public List<User> findByConfirmedFalse() {
        return userRepo.findByConfirmedFalse();
    }

    @Override
    public List<User> findByBlockedTrue() {
        return userRepo.findByBlockedTrue();
    }

    @Override
    public List<User> findByBlockedFalse() {
        return userRepo.findByBlockedFalse();
    }

    @Override
    public List<User> findByAdministratorTrue() {
        return userRepo.findByAdministratorTrue();
    }

    @Override
    public List<User> findByAdministratorFalse() {
        return userRepo.findByAdministratorFalse();
    }
}