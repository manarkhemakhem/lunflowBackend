package com.example.lunflow.dao.Repository;

import com.example.lunflow.dao.Model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepo extends MongoRepository<User, String> {

    List<User> findByConfirmedTrue();
    List<User> findByConfirmedFalse();

    List<User> findByBlockedTrue();
    List<User> findByBlockedFalse();

    List<User> findByAdministratorTrue();
    List<User> findByAdministratorFalse();
}
