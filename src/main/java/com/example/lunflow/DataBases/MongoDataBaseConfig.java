package com.example.lunflow.DataBases;

import com.example.lunflow.dao.Model.Collaborator;
import com.example.lunflow.dao.Model.Group;
import com.example.lunflow.dao.Model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class MongoDataBaseConfig {
    @Autowired
    private MongoTemplate mongoTemplate;

    // Lire les configurations des bases de données MongoDB depuis un fichier JSON
    public List<Database> loadDatabaseConfigs() {
        try {
            //          → Paths.get("") → ça veut dire le répertoire courant (là où ton programme tourne).

            // → .toAbsolutePath() → transforme ce chemin relatif en chemin absolu.

            // → .toString() → convertit le chemin en texte (String) pour le donner au constructeur File.

            File jarDir = new File(Paths.get("").toAbsolutePath().toString());

            // Indique l'emplacement du fichier JSON contenant les configs des bases
            //  ici on est entraint  de construire un chemin /parent/child (database.json)

            File configFile = new File(jarDir, "src/main/resources/database.json");

            // Vérifie si le fichier existe
            if (!configFile.exists()) {
                throw new RuntimeException("Configuration file not found: " + configFile.getAbsolutePath());
            }

            // Lire et convertir le fichier JSON en objet Java
            ObjectMapper objectMapper = new ObjectMapper();
            DatabaseConfigData config = objectMapper.readValue(configFile, DatabaseConfigData.class);

            return config.getDatabases(); // Retourne la liste des bases de données

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
    } public List<Database> getAllDatabases() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        // Récupérer le chemin du fichier JSON
        File jarDir = new File(Paths.get("").toAbsolutePath().toString());
        File configFile = new File(jarDir, "src/main/resources/database.json");

        // Lire le JSON et le convertir en liste d'objets Database
        return Arrays.asList(objectMapper.readValue(configFile, Database[].class));
    }
    public List<Database> getDataFromDatabase(Database database, String collectionName) {
        // Vérifier si la base de données existe
        if (mongoTemplate.getDb().getName().equals(database.getName())) {
            // Récupérer les données de la collection spécifiée
            return mongoTemplate.findAll(Database.class, collectionName);
        } else {
            throw new IllegalArgumentException("Base de données non trouvée : " + database.getName());
        }
    }

    Class<?> getClassForCollection(String collectionName) {
        return switch (collectionName.toLowerCase()) {
            case "collaborators" -> Collaborator.class;
            case "user " -> User.class;
            case "goup" -> Group.class;
            default -> throw new IllegalArgumentException("Collection inconnue : " + collectionName);
        };
    }
    public List<?> getDataFrom(String dbName, String collectionName) {
        MongoTemplate template = getMongoTemplateForDatabase(dbName);
        Class<?> clazz = getClassForCollection(collectionName);
        return template.findAll(clazz);
    }
    private MongoTemplate getMongoTemplateForDatabase(String dbName) {
        // Crée ou récupère un MongoTemplate dynamique en fonction du nom de la base
        MongoClient client = MongoClients.create("mongodb://localhost:27017");
        return new MongoTemplate(client, dbName);
    }
}
