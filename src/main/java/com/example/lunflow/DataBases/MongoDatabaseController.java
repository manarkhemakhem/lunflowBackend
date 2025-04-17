package com.example.lunflow.DataBases;

import com.example.lunflow.dao.Model.Collaborator;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/database")

public class MongoDatabaseController {
    @Autowired
    private final MongoDataBaseConfig mongoDataBaseConfig;

    public MongoDatabaseController(MongoDataBaseConfig mongoDataBaseConfig) {
        this.mongoDataBaseConfig = mongoDataBaseConfig;
    }

    //     Endpoint pour tester les connexions à toutes les bases de données

    @GetMapping("/all")
    public List<Database> getAllDatabases() {
        System.out.println("=== loadDatabaseConfigs ===");
        return mongoDataBaseConfig.loadDatabaseConfigs();
    }

    @GetMapping("/{databaseName}/{collectionName}")
    public List<?> getDatabaseData(
            @PathVariable String databaseName,
            @PathVariable String collectionName) {

        // Crée un client MongoDB pour la base de données spécifiée
        MongoClient client = MongoClients.create("mongodb://localhost:27017");
        MongoTemplate template = new MongoTemplate(client, databaseName);

        // Détermine la classe Java correspondant à la collection
        Class<?> clazz = mongoDataBaseConfig.getClassForCollection(collectionName);

        // Récupère tous les documents de la collection
        return template.findAll(clazz, collectionName);
    }
    @GetMapping("/{databaseName}/filter")
    public List<?> filterByField(
            @PathVariable String databaseName,
            @RequestParam String collection,
            @RequestParam String field,
            @RequestParam String value
    ) {
        return mongoDataBaseConfig.filterByField(databaseName, collection, field, value);
    }

    @GetMapping("/testConnection/{databaseName}")
    public ResponseEntity<String> testConnection(@PathVariable String databaseName) {
        System.out.println("🔍 Testing connection for: " + databaseName);

        Database database = mongoDataBaseConfig.findDatabaseByName(databaseName);
        if (database == null) {
            System.out.println("❌ Database not found: " + databaseName);
            return ResponseEntity.status(404).body("Database with name '" + databaseName + "' not found.");
        }

        boolean isConnected = mongoDataBaseConfig.testConnection(database);
        if (isConnected) {
            System.out.println("✅ Connection successful: " + databaseName);
            return ResponseEntity.ok("Connection to database '" + databaseName + "' successful.");
        } else {
            System.out.println("⚠️ Connection failed: " + databaseName);
            return ResponseEntity.status(500).body("Connection to database '" + databaseName + "' failed.");
        }
    }

}



