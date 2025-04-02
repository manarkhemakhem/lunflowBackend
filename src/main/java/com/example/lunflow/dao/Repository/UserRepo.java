package com.example.lunflow.dao.Repository;

import com.example.lunflow.dao.Model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepo extends MongoRepository<User, String> {
}
