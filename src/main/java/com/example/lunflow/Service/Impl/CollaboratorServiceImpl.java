package com.example.lunflow.Service.Impl;

import com.example.lunflow.DataBases.MongoDataBaseConfig;
import com.example.lunflow.Service.CollaboratorService;
import com.example.lunflow.dao.Model.Collaborator;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CollaboratorServiceImpl implements CollaboratorService {

    private final MongoDataBaseConfig mongoDataBaseConfig;

    @Autowired
    public CollaboratorServiceImpl(MongoDataBaseConfig mongoDataBaseConfig) {
        this.mongoDataBaseConfig = mongoDataBaseConfig;
    }
    private MongoTemplate mongoTemplate;

    @Override
    public List<Collaborator> getAllCollaborators(String databaseName) {
        MongoTemplate mongoTemplate = mongoDataBaseConfig.getMongoTemplateForDatabase(databaseName);
        return mongoTemplate.findAll(Collaborator.class, "collaborator");
    }

    @Override
    public Collaborator getCollaboratorById(String databaseName, String id) {
        MongoTemplate mongoTemplate = mongoDataBaseConfig.getMongoTemplateForDatabase(databaseName);
        return mongoTemplate.findById(id, Collaborator.class, "collaborator");
    }

    @Override
    public List<Collaborator> getCollaboratorByGroupId(String databaseName, String groupId) {
        MongoTemplate mongoTemplate = mongoDataBaseConfig.getMongoTemplateForDatabase(databaseName);
        Query query = new Query(Criteria.where("groupId").is(groupId));
        return mongoTemplate.find(query, Collaborator.class, "collaborator");
    }

    @Override
    public Map<String, Long> getCreationDatesHistogram(String databaseName) {
        MongoTemplate mongoTemplate = mongoDataBaseConfig.getMongoTemplateForDatabase(databaseName);
        List<Collaborator> collaborators = mongoTemplate.findAll(Collaborator.class, "collaborator");

        return collaborators.stream()
                .map(Collaborator::getId)
                .filter(Objects::nonNull)
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
                    int quarter = (date.getMonthValue() - 1) / 3 + 1;
                    return year + "-T" + quarter;
                })
                .collect(Collectors.groupingBy(q -> q, Collectors.counting()));
    }

    @Override
    public Map<String, Long> countByField(String databaseName, String fieldName) {
        MongoTemplate mongoTemplate = mongoDataBaseConfig.getMongoTemplateForDatabase(databaseName);

        AggregationExpression ifNullExpression = context -> new org.bson.Document(
                "$ifNull",
                Arrays.asList("$" + fieldName, false)
        );

        ProjectionOperation project = Aggregation.project()
                .and(ifNullExpression).as("fieldValue");

        GroupOperation group = Aggregation.group("fieldValue").count().as("count");

        Aggregation aggregation = Aggregation.newAggregation(project, group);

        AggregationResults<org.bson.Document> results = mongoTemplate.aggregate(
                aggregation, "collaborator", org.bson.Document.class
        );

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
    public List<Collaborator> getCollaboratorIsAdmin(String databaseName) {
        MongoTemplate mongoTemplate = mongoDataBaseConfig.getMongoTemplateForDatabase(databaseName);
        Query query = new Query(Criteria.where("isAdmin").is(true));
        return mongoTemplate.find(query, Collaborator.class, "collaborator");
    }

    @Override
    public List<Collaborator> getCollaboratorIsAdminFalse(String databaseName) {
        MongoTemplate mongoTemplate = mongoDataBaseConfig.getMongoTemplateForDatabase(databaseName);
        Query query = new Query();
        query.addCriteria(new Criteria().orOperator(
                Criteria.where("isAdmin").is(false),
                Criteria.where("isAdmin").exists(false)
        ));
        return mongoTemplate.find(query, Collaborator.class, "collaborator");
    }

    @Override
    public List<Collaborator> getCollaboratoronline(String databaseName) {
        MongoTemplate mongoTemplate = mongoDataBaseConfig.getMongoTemplateForDatabase(databaseName);
        Query query = new Query(Criteria.where("onLine").is(true));
        return mongoTemplate.find(query, Collaborator.class, "collaborator");
    }

    @Override
    public List<Collaborator> getCollaboratoroffline(String databaseName) {
        MongoTemplate mongoTemplate = mongoDataBaseConfig.getMongoTemplateForDatabase(databaseName);
        Query query = new Query();
        query.addCriteria(new Criteria().orOperator(
                Criteria.where("onLine").is(false),
                Criteria.where("onLine").exists(false)
        ));
        return mongoTemplate.find(query, Collaborator.class, "collaborator");
    }

    @Override
    public Boolean getCollaboratorDeletedStatus(String databaseName, String collaboratorId) {
        MongoTemplate mongoTemplate = mongoDataBaseConfig.getMongoTemplateForDatabase(databaseName);
        Collaborator collaborator = mongoTemplate.findById(collaboratorId, Collaborator.class, "collaborator");
        if (collaborator == null) {
            return null;
        }
        return (collaborator.getDeleted() != null) ? collaborator.getDeleted() : true;
    }

    @Override
    public List<Collaborator> searchByFullnameRegexIgnoreCase(String databaseName, String fullname) {
        if (fullname == null || fullname.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom ne peut pas être vide ou null");
        }
        MongoTemplate mongoTemplate = mongoDataBaseConfig.getMongoTemplateForDatabase(databaseName);
        Query query = new Query(Criteria.where("fullname").regex(fullname.trim(), "i"));
        return mongoTemplate.find(query, Collaborator.class, "collaborator");
    }
//    public List<Collaborator> filterDynamic(String field, String operator, String valueStr) {
//        Criteria criteria;
//
//        // Tentative de conversion en nombre ou date
//        Object value = parseValue(valueStr);
//
//        switch (operator.toLowerCase()) {
//            case "equals":
//                criteria = Criteria.where(field).is(value);
//                break;
//            case "not equals":
//                criteria = Criteria.where(field).ne(value);
//                break;
//            case "contains":criteria = Criteria.where(field).regex(".*" + valueStr + ".*", "i");
//                break;
//
//            case "starts with":
//                criteria = Criteria.where(field).regex("^" + valueStr, "i");
//                break;
//            case "ends with":
//                criteria = Criteria.where(field).regex(valueStr + "$", "i");
//                break;
//            case "greater than":
//                criteria = Criteria.where(field).gt(value);
//                break;
//            case "less than":
//                criteria = Criteria.where(field).lt(value);
//                break;
//            default:
//                throw new IllegalArgumentException("Unsupported operator: " + operator);
//        }

//        Query query = new Query(criteria);
//        return mongoTemplate.find(query, Collaborator.class);
//    }

//    private Object parseValue(String valueStr) {
//        // Tentative de conversion nombre
//        try {
//            return Integer.parseInt(valueStr);
//        } catch (NumberFormatException ignored) {}
//
//        // Tentative de conversion date
//        try {
//            return new SimpleDateFormat("yyyy-MM-dd").parse(valueStr);
//        } catch (Exception ignored) {}
//
//        // Sinon on retourne la chaîne brute
//        return valueStr;
//    }

}