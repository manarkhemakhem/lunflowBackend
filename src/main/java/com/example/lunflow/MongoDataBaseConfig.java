package com.example.lunflow;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

@Component
public class MongoDataBaseConfig {
    @Autowired
    private MongoTemplate mongoTemplate;

    // Charger la liste des configurations des bases de données
    public List<Database> loadDatabaseConfigs() {
        try {
            // Assure-toi que le fichier est dans le bon répertoire
            File jarDir = new File(Paths.get("").toAbsolutePath().toString());
            File configFile = new File(jarDir, "src/main/resources/database.json");

            if (!configFile.exists()) {
                throw new RuntimeException("Configuration file not found: " + configFile.getAbsolutePath());
            }

            ObjectMapper objectMapper = new ObjectMapper();
            DatabaseConfigData config = objectMapper.readValue(configFile, DatabaseConfigData.class);

            return config.getDatabases();  // Récupérer la liste des bases de données à partir de l'objet

        } catch (IOException e) {
            throw new RuntimeException("Error reading configuration file", e);
        }
    }

    public boolean testConnection(Database db) {
        try (MongoClient mongoClient = MongoClients.create(db.getUri())) {
            MongoDatabase database = mongoClient.getDatabase(db.getName());
            // Effectuer une simple requête pour vérifier la connexion
            database.listCollections().first(); // Cela permet de tester la connexion
            System.out.println("Connexion réussie à : " + db.getName());
            return true;
        } catch (Exception e) {
            System.out.println("Échec de la connexion à : " + db.getName());
            e.printStackTrace();
            return false;
        }
    }
    public Database findDatabaseByName(String databaseName) {
        List<Database> databases = loadDatabaseConfigs();

        for (Database db : databases) {
            if (db.getName().equalsIgnoreCase(databaseName)) {
                return db;
            }
        }
        return null;
    }
}
