package com.example.lunflow.Service.Impl;

import com.example.lunflow.Service.UserService;
import com.example.lunflow.dao.Model.User;
import com.example.lunflow.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.stereotype.Service;
import org.springframework.data.mongodb.core.aggregation.Aggregation;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;

@Service
public class UserServiceImpl  implements UserService {
    private final UserDao userDao;
    @Autowired
    private MongoTemplate mongoTemplate;

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



    @Override

    public List<LocalDateTime> getAllcreationDate() {
        List<User> users = userDao.findAll();  // Récupère tous les utilisateurs
        return users.stream()                        // Parcourt chaque utilisateur
                .map(User::getcreationDate)    // Récupère la date de création
                .collect(Collectors.toList());  // Retourne une liste de dates
    }

}
