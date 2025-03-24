package com.example.lunflow.Service.Impl;

import com.example.lunflow.Service.CollaboratorService;
import com.example.lunflow.dao.CollaboratorDao;
import com.example.lunflow.dao.Model.Collaborator;
import com.example.lunflow.dao.Repository.CollaboratorRepo;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service

public class CollaboratorServiceImpl  implements CollaboratorService {
    private final CollaboratorDao collaboratorDao;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Override
    public Map<LocalDate, Long> getCreationDatesHistogram() {
        List<Collaborator> collaborators = collaboratorDao.findAll();

        return collaborators.stream()
                .map(Collaborator::getId) // Récupérer l'ID de chaque collaborateur
                .filter(id -> id != null) // Vérifier qu'il n'est pas null
                .map(id -> (id instanceof ObjectId) ? ((ObjectId) id).getTimestamp() : new ObjectId(id.toString()).getTimestamp()) // Gérer ObjectId
                .map(timestamp -> Instant.ofEpochSecond(timestamp).atZone(ZoneId.systemDefault()).toLocalDate()) // Conversion timestamp -> LocalDate
                .collect(Collectors.groupingBy(date -> date, Collectors.counting())); // Grouper par date
    }
    public CollaboratorServiceImpl(CollaboratorDao collaboratorDao) {
        this.collaboratorDao = collaboratorDao;
    }

    @Override
    public List<Collaborator> getAllCollaborators() {
        return collaboratorDao.findAll();
    }

    @Override
    public Collaborator getCollaboratorById(String id) {
        return collaboratorDao.findById(id);
    }

    @Override
    public List<Collaborator> getCollaboratorByGroupId(String groupId) {
        return collaboratorDao.findByGroupId(groupId);
    }
    @Override
    public List<Collaborator> getCollaboratorIsAdmin() {
        // Création d'une Query avec un critère où isAdmin est true
        Query query = new Query();
        query.addCriteria(Criteria.where("isAdmin").is(true));
        return mongoTemplate.find(query, Collaborator.class);
    }
    @Override
    public List<Collaborator> getCollaboratorIsAdminFalse() {
        // Création d'une Query avec un critère où isAdmin est false ou non défini
        Query query = new Query();

        // Utilisation de orOperator pour combiner les critères
        query.addCriteria(new Criteria().orOperator(
                Criteria.where("isAdmin").is(false),  // Cherche où isAdmin est false
                Criteria.where("isAdmin").exists(false)  // Cherche où isAdmin n'existe pas
        ));

        // Exécution de la requête et récupération des résultats
        return mongoTemplate.find(query, Collaborator.class);
    }



}
