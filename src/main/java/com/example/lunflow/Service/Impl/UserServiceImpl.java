package com.example.lunflow.Service.Impl;

import com.example.lunflow.Service.UserService;
import com.example.lunflow.dao.Model.User;
import com.example.lunflow.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.stereotype.Service;
import org.springframework.data.mongodb.core.aggregation.Aggregation;

import java.util.List;


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

    public List<User> getUsersGroupedByQuarter() {
        Aggregation aggregation = Aggregation.newAggregation(
                project()
                        .andExpression("creationDate.getMonth()").as("month")
                        .andExpression("creationDate.getYear()").as("year")
                        .and("creationDate").as("creationDate"),
                group("year", "month")
                        .count().as("userCount")
                        .push("creationDate").as("dates"),
                project("userCount", "dates")
        );

        AggregationResults<User> results = mongoTemplate.aggregate(aggregation, User.class, User.class);
        return results.getMappedResults();
    }

}
