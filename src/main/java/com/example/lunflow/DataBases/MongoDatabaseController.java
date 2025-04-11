package com.example.lunflow.DataBases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/database")

public class MongoDatabaseController {
    @Autowired
    private final  MongoDataBaseConfig mongoDataBaseConfig;

    public MongoDatabaseController(MongoDataBaseConfig mongoDataBaseConfig) {
        this.mongoDataBaseConfig = mongoDataBaseConfig;
    }

    //     Endpoint pour tester les connexions à toutes les bases de données
    @GetMapping("/testConnection/{databaseName}")
    public String testConnection(@PathVariable String databaseName) {
        // Trouver la base de données par son nom
        Database database = mongoDataBaseConfig.findDatabaseByName(databaseName);

        if (database == null) {
            return "Database with name '" + databaseName + "' not found.";
        }

        // Tester la connexion à la base de données
        boolean isConnected = mongoDataBaseConfig.testConnection(database);
        if (isConnected) {
            return "Connection to database '" + databaseName + "' successful.";
        } else {
            return "Connection to database '" + databaseName + "' failed.";
        }
    }
    @GetMapping("/all")
    public List<Database> getAllDatabases() {
        System.out.println("=== loadDatabaseConfigs ===");
        return mongoDataBaseConfig.loadDatabaseConfigs();
    }

}



