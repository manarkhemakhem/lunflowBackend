package com.example.lunflow.dao;

import com.example.lunflow.dao.Model.Collaborator;
import com.example.lunflow.dao.Repository.CollaboratorRepo;

import java.util.List;

public interface CollaboratorDao

{

   List<Collaborator> findAll();
   Collaborator findById(String id);

    List<Collaborator> findByGroupId(String groupId);

    //Collaborator findByEmail(String email);

    List<Collaborator> findByIsAdmin(boolean isAdmin);
    List<Collaborator> findByIsAdminFalse(boolean isAdmin);

    List<Collaborator> searchByFullnameRegexIgnoreCase(String fullname);

    void deleteById(String id);
}

