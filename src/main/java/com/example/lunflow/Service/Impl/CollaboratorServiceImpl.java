package com.example.lunflow.Service.Impl;

import com.example.lunflow.Service.CollaboratorService;
import com.example.lunflow.dao.CollaboratorDao;
import com.example.lunflow.dao.Model.Collaborator;
import com.example.lunflow.dao.Repository.CollaboratorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
@Service

public class CollaboratorServiceImpl  implements CollaboratorService {
    private final CollaboratorDao collaboratorDao;
    @Autowired
    private MongoTemplate mongoTemplate;

    public CollaboratorServiceImpl(CollaboratorDao collaboratorDao) {
        this.collaboratorDao = collaboratorDao;
    }


    public List<Collaborator> getAllCollaborators() {
        return collaboratorDao.findAll();
    }

    public Collaborator getCollaboratorById(String id) {
        return collaboratorDao.findById(id);
    }

    @Override
    public List<Collaborator> getCollaboratorByGroupId(String groupId) {
        return collaboratorDao.findByGroupId(groupId);
    }

    public List<Collaborator> getCollaboratorIsAdmin() {
        // Création d'une Query avec un critère où isAdmin est true
        Query query = new Query();
        query.addCriteria(Criteria.where("isAdmin").is(true));
        return mongoTemplate.find(query, Collaborator.class);
    }


    public List<Collaborator> getCollaboratorIsAdminFalse() {
        // Création d'une Query avec un critère où isAdmin est false ou non défini
        Query query = new Query();
        query.addCriteria(Criteria.where("isAdmin").is(false).orOperator(Criteria.where("isAdmin").exists(false)));
        return mongoTemplate.find(query, Collaborator.class);

    }

}
