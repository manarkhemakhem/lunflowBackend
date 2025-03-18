package com.example.lunflow.dao.Repository;

import com.example.lunflow.dao.Model.Collaborator;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CollaboratorRepo extends MongoRepository<Collaborator, String> {

    List<Collaborator> findByGroupId(String groupId);
    List<Collaborator> findByIsAdmin(boolean isAdmin);
    List<Collaborator> findByIsAdminFalse(boolean isAdmin);



}



