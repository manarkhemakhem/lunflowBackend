package com.example.lunflow.DataBases;

import com.example.lunflow.dao.Model.Collaborator;
import com.example.lunflow.dao.Model.Group;
import com.example.lunflow.dao.Model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class MongoDataBaseConfig {

    // Stocke les configurations des bases de données en mémoire
    private final Map<String, Database> databaseConfigMap = new ConcurrentHashMap<>();

    // Cache pour les MongoTemplate
    private final Map<String, MongoTemplate> mongoTemplateCache = new ConcurrentHashMap<>();

    // Nom de la base de données par défaut (configurable via application.properties)
    @Value("${spring.data.mongodb.database:testmanar}")
    private String defaultDatabaseName;

    // Chemin du fichier database.json
    @Value("classpath:database.json")
    private Resource databaseJson;

    // Classe interne pour mapper database.json
    public static class DatabaseConfigData {
        private List<Database> databases;

        public List<Database> getDatabases() {
            return databases;
        }

        public void setDatabases(List<Database> databases) {
            this.databases = databases;
        }
    }

    public static class Database {
        private String name;
        private String uri;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }
    }

    // Charge les configurations au démarrage
    @PostConstruct
    public void loadDatabaseConfigs() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        DatabaseConfigData config = objectMapper.readValue(databaseJson.getInputStream(), DatabaseConfigData.class);
        for (Database db : config.getDatabases()) {
            databaseConfigMap.put(db.getName(), db);
        }
    }

    // Crée un MongoClient unique
    @Bean
    public MongoClient mongoClient() {
        // Utilise l'URI de la base de données par défaut ou une URI générique
        Database defaultDb = databaseConfigMap.getOrDefault(defaultDatabaseName, new Database());
        defaultDb.setUri(defaultDb.getUri() != null ? defaultDb.getUri() : "mongodb://localhost:27017");
        return MongoClients.create(defaultDb.getUri());
    }

    // Crée un MongoTemplate pour la base de données par défaut
    @Bean(name = "mongoTemplate")
    public MongoTemplate mongoTemplate(MongoClient mongoClient) {
        Database db = databaseConfigMap.get(defaultDatabaseName);
        if (db == null) {
            throw new IllegalArgumentException("Base de données par défaut non trouvée : " + defaultDatabaseName);
        }
        return new MongoTemplate(mongoClient, db.getName());
    }

    // Obtient un MongoTemplate pour une base de données spécifique
    public MongoTemplate getMongoTemplateForDatabase(String dbName) {
        return mongoTemplateCache.computeIfAbsent(dbName, name -> {
            Database db = databaseConfigMap.get(name);
            if (db == null) {
                throw new IllegalArgumentException("Base de données non trouvée : " + name);
            }
            return new MongoTemplate(MongoClients.create(db.getUri()), db.getName());
        });
    }

    // Teste la connexion à une base de données
    public boolean testConnection(String dbName) {
        Database db = databaseConfigMap.get(dbName);
        if (db == null) {
            System.out.println("Base de données non trouvée : " + dbName);
            return false;
        }
        try (MongoClient mongoClient = MongoClients.create(db.getUri())) {
            MongoDatabase database = mongoClient.getDatabase(db.getName());
            database.listCollections().first(); // Teste la connexion
            System.out.println("Connexion réussie à : " + db.getName());
            return true;
        } catch (Exception e) {
            System.out.println("Échec de la connexion à : " + db.getName());
            e.printStackTrace();
            return false;
        }
    }

    // Récupère toutes les bases de données
    public List<Database> getAllDatabases() {
        return new ArrayList<>(databaseConfigMap.values());
    }

    // Récupère les données d'une collection dans une base de données
    public List<?> getDataFromDatabase(String dbName, String collectionName) {
        MongoTemplate dynamicTemplate = getMongoTemplateForDatabase(dbName);
        Class<?> clazz = getClassForCollection(collectionName);
        return dynamicTemplate.findAll(clazz, collectionName);
    }

    // Filtre les données par champ
    public List<?> filterByField(String dbName, String collection, String field, String value) {
        MongoTemplate template = getMongoTemplateForDatabase(dbName);
        Class<?> clazz = getClassForCollection(collection);

        Query query = new Query();
        Object typedValue = value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")
                ? Boolean.parseBoolean(value)
                : value;
        query.addCriteria(Criteria.where(field).is(typedValue));
        return template.find(query, clazz, collection);
    }

    // Mappe une collection à une classe
    private Class<?> getClassForCollection(String collectionName) {
        return switch (collectionName.toLowerCase()) {
            case "collaborator" -> Collaborator.class;
            case "user" -> User.class;
            case "group" -> Group.class;
            default -> throw new IllegalArgumentException("Collection inconnue : " + collectionName);
        };
    }

    // Récupère les données d'une collection
    public List<?> getDataFrom(String dbName, String collectionName) {
        MongoTemplate template = getMongoTemplateForDatabase(dbName);
        Class<?> clazz = getClassForCollection(collectionName);
        return template.findAll(clazz, collectionName);
    }

    // Recherche une base de données par nom
    public Database findDatabaseByName(String databaseName) {
        return databaseConfigMap.get(databaseName);
    }

    public void doSomething() {
        System.out.println("MongoDatabaseManager is working!");
    }
}