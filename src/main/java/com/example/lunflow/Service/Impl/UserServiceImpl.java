package com.example.lunflow.Service.Impl;

import com.example.lunflow.DataBases.MongoDataBaseConfig;
import com.example.lunflow.Service.UserService;
import com.example.lunflow.dao.Model.User;
import com.example.lunflow.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final MongoDataBaseConfig mongoDataBaseConfig;

    @Autowired
    public UserServiceImpl(UserDao userDao, MongoDataBaseConfig mongoDataBaseConfig) {
        this.userDao = userDao;
        this.mongoDataBaseConfig = mongoDataBaseConfig;
    }

    @Override
    public List<User> getAllUsers(String databaseName) {
        Objects.requireNonNull(databaseName, "Database name cannot be null");
        MongoTemplate mongoTemplate = mongoDataBaseConfig.getMongoTemplateForDatabase(databaseName);
        List<User> users = mongoTemplate.findAll(User.class, "user");
        System.out.println("UserServiceImpl: Fetched " + users.size() + " users from database '" + databaseName + "'");
        return users;
    }

    @Override
    public User getUserById(String databaseName, String id) {
        Objects.requireNonNull(databaseName, "Database name cannot be null");
        Objects.requireNonNull(id, "ID cannot be null");
        MongoTemplate mongoTemplate = mongoDataBaseConfig.getMongoTemplateForDatabase(databaseName);
        User user = mongoTemplate.findById(id, User.class, "user");
        System.out.println("UserServiceImpl: Fetched user ID '" + id + "' from database '" + databaseName + "': " + (user != null ? user.getUsername() : "null"));
        return user;
    }

    @Override
    public List<User> getAllConfirmedTrue(String databaseName) {
        Objects.requireNonNull(databaseName, "Database name cannot be null");
        MongoTemplate mongoTemplate = mongoDataBaseConfig.getMongoTemplateForDatabase(databaseName);
        return mongoTemplate.find(org.springframework.data.mongodb.core.query.Query.query(
                        org.springframework.data.mongodb.core.query.Criteria.where("confirmed").is(true)),
                User.class, "user");
    }

    @Override
    public List<User> getAllConfirmedFalse(String databaseName) {
        Objects.requireNonNull(databaseName, "Database name cannot be null");
        MongoTemplate mongoTemplate = mongoDataBaseConfig.getMongoTemplateForDatabase(databaseName);
        return mongoTemplate.find(org.springframework.data.mongodb.core.query.Query.query(
                        org.springframework.data.mongodb.core.query.Criteria.where("confirmed").is(false)),
                User.class, "user");
    }

    @Override
    public List<User> getAllBlockedTrue(String databaseName) {
        Objects.requireNonNull(databaseName, "Database name cannot be null");
        MongoTemplate mongoTemplate = mongoDataBaseConfig.getMongoTemplateForDatabase(databaseName);
        return mongoTemplate.find(org.springframework.data.mongodb.core.query.Query.query(
                        org.springframework.data.mongodb.core.query.Criteria.where("blocked").is(true)),
                User.class, "user");
    }

    @Override
    public List<User> getAllBlockedFalse(String databaseName) {
        Objects.requireNonNull(databaseName, "Database name cannot be null");
        MongoTemplate mongoTemplate = mongoDataBaseConfig.getMongoTemplateForDatabase(databaseName);
        return mongoTemplate.find(org.springframework.data.mongodb.core.query.Query.query(
                        org.springframework.data.mongodb.core.query.Criteria.where("blocked").is(false)),
                User.class, "user");
    }

    @Override
    public List<User> getAllAdminTrue(String databaseName) {
        Objects.requireNonNull(databaseName, "Database name cannot be null");
        MongoTemplate mongoTemplate = mongoDataBaseConfig.getMongoTemplateForDatabase(databaseName);
        return mongoTemplate.find(org.springframework.data.mongodb.core.query.Query.query(
                        org.springframework.data.mongodb.core.query.Criteria.where("administrator").is(true)),
                User.class, "user");
    }

    @Override
    public List<User> getAllAdminFalse(String databaseName) {
        Objects.requireNonNull(databaseName, "Database name cannot be null");
        MongoTemplate mongoTemplate = mongoDataBaseConfig.getMongoTemplateForDatabase(databaseName);
        return mongoTemplate.find(org.springframework.data.mongodb.core.query.Query.query(
                        org.springframework.data.mongodb.core.query.Criteria.where("administrator").is(false)),
                User.class, "user");
    }

    @Override
    public List<String> getAllCreationDate(String databaseName) {
        Objects.requireNonNull(databaseName, "Database name cannot be null");
        MongoTemplate mongoTemplate = mongoDataBaseConfig.getMongoTemplateForDatabase(databaseName);
        List<User> users = mongoTemplate.findAll(User.class, "user");
        List<String> creationDates = users.stream()
                .map(user -> user.getCreationDate().toString())
                .collect(Collectors.toList());
        System.out.println("UserServiceImpl: Fetched " + creationDates.size() + " creation dates from database '" + databaseName + "'");
        return creationDates;
    }
}