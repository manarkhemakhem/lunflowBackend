package com.example.lunflow.Service.Impl;

import com.example.lunflow.Service.UserService;
import com.example.lunflow.dao.Model.User;
import com.example.lunflow.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl  implements UserService {
    private final UserDao userDao;
    @Autowired
    public UserServiceImpl (UserDao userDao){
        this.userDao=userDao;
    }

    @Override
    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    @Override
    public User getUserById(String id) {
        return userDao.findById(id);
    }

    @Override
    public List<User> getAllConfirmedTrue() {
        return userDao.findByConfirmedTrue();
    }

    @Override
    public List<User> getAllConfirmedFalse() {
        return userDao.findByConfirmedFalse();
    }

    @Override
    public List<User> getAllBlockedTrue() {
        return userDao.findByBlockedTrue();
    }

    @Override
    public List<User> getAllBlockedFalse() {
        return userDao.findByBlockedFalse();
    }

    @Override
    public List<User> getAllAdminTrue() {
        return userDao.findByAdministratorTrue();
    }

    @Override
    public List<User> getAllAdminFalse() {
        return userDao.findByAdministratorFalse();
    }


}
