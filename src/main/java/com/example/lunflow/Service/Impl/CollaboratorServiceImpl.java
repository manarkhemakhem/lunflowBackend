package com.example.lunflow.Service.Impl;

import com.example.lunflow.Service.CollaboratorService;
import com.example.lunflow.dao.CollaboratorDao;
import com.example.lunflow.dao.Model.Collaborator;
import com.example.lunflow.dao.Repository.CollaboratorRepo;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.bson.Document;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service

public class CollaboratorServiceImpl  implements CollaboratorService {
    private final CollaboratorDao collaboratorDao;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Override
    public Map<String, Long> getCreationDatesHistogram() {
        List<Collaborator> collaborators = collaboratorDao.findAll();

        return collaborators.stream()
                .map(Collaborator::getId)
                .filter(Objects::nonNull) // Vérification simplifiée
                .map(id -> {
                    if (id instanceof ObjectId) {
                        return ((ObjectId) id).getTimestamp();
                    } else {
                        return new ObjectId(id.toString()).getTimestamp();
                    }
                })
                .map(timestamp -> Instant.ofEpochSecond(timestamp).atZone(ZoneId.systemDefault()).toLocalDate())
                .map(date -> {
                    int year = date.getYear();
                    int quarter = (date.getMonthValue() - 1) / 3 + 1; // Convertir mois en trimestre
                    return year + "-T" + quarter; // Format "2025-T1"
                })
                .collect(Collectors.groupingBy(q -> q, Collectors.counting()));
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
    public Map<String, Long> countByField(String fieldName) {
        // Utilisation de AggregationExpression pour injecter une expression MongoDB
        AggregationExpression ifNullExpression = context -> new org.bson.Document(
                "$ifNull",
                Arrays.asList("$" + fieldName, false)
        );

        // Étape 1 : Projection
        ProjectionOperation project = Aggregation.project()
                .and(ifNullExpression).as("fieldValue");

        // Étape 2 : Groupement par la valeur projetée
        GroupOperation group = Aggregation.group("fieldValue").count().as("count");

        // Étape 3 : Agrégation
        Aggregation aggregation = Aggregation.newAggregation(project, group);

        AggregationResults<org.bson.Document> results = mongoTemplate.aggregate(
                aggregation, "collaborateurs", org.bson.Document.class
        );

        // Étape 4 : Mapping des résultats
        Map<String, Long> response = new HashMap<>();
        for (org.bson.Document doc : results) {
            Object key = doc.get("_id");
            Object count = doc.get("count");
            if (key != null && count != null) {
                response.put(key.toString(), Long.parseLong(count.toString()));
            }
        }

        return response;
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

    @Override
    public List<Collaborator> getCollaboratoronline() {
        // Création d'une Query avec un critère où onLine est true
        Query query = new Query();
        query.addCriteria(Criteria.where("onLine").is(true));

        // Exécution de la requête et récupération des résultats
        return mongoTemplate.find(query, Collaborator.class);
    }
    @Override
    public List<Collaborator> getCollaboratoroffline() {
        // Création d'une Query avec un critère où onLine est false ou non défini
        Query query = new Query();

        // Utilisation de orOperator pour combiner les critères
        query.addCriteria(new Criteria().orOperator(
                Criteria.where("onLine").is(false),  // Cherche où onLine est false
                Criteria.where("onLine").exists(false)  // Cherche où onLine n'existe pas
        ));

        // Exécution de la requête et récupération des résultats
        return mongoTemplate.find(query, Collaborator.class);
    }
    @Override
    public Boolean getCollaboratorDeletedStatus(String collaboratorId) {
        Collaborator collaborator = collaboratorDao.findById(collaboratorId);

        if (collaborator == null) {
            return null;
        }

        return (collaborator.getDeleted() != null) ? collaborator.getDeleted() : true;
    }
    @Override
    public List<Collaborator> searchByFullnameRegexIgnoreCase(String fullname) {
        if (fullname == null || fullname.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom ne peut pas être vide ou null");
        }
        return collaboratorDao.searchByFullnameRegexIgnoreCase(fullname.trim());
    }




}
