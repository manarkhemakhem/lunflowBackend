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

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/database")
public class MongoDatabaseController {

    private final MongoDataBaseConfig mongoDataBaseConfig;

    @Autowired
    public MongoDatabaseController(MongoDataBaseConfig mongoDataBaseConfig) {
        this.mongoDataBaseConfig = mongoDataBaseConfig;
    }

    // Endpoint pour récupérer toutes les bases de données configurées
    @GetMapping("/all")
    public ResponseEntity<List<MongoDataBaseConfig.Database>> getAllDatabases() {
        try {
            List<MongoDataBaseConfig.Database> databases = mongoDataBaseConfig.getAllDatabases();
            return ResponseEntity.ok(databases);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Erreur lors de la récupération des bases de données : " + e.getMessage());
        }
    }

    // Endpoint pour récupérer les données d'une collection dans une base de données
    @GetMapping("/{databaseName}/{collectionName}")
    public ResponseEntity<List<?>> getDatabaseData(
            @PathVariable String databaseName,
            @PathVariable String collectionName) {
        try {
            // Vérifie si la base de données existe
            MongoDataBaseConfig.Database database = mongoDataBaseConfig.findDatabaseByName(databaseName);
            if (database == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Base de données non trouvée : " + databaseName);
            }

            // Récupère les données à l'aide de MongoTemplate configuré dynamiquement
            List<?> data = mongoDataBaseConfig.getDataFrom(databaseName, collectionName);
            return ResponseEntity.ok(data);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Collection invalide : " + e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Erreur lors de la récupération des données : " + e.getMessage());
        }
    }

    // Endpoint pour filtrer les données par champ
    @GetMapping("/{databaseName}/filter")
    public ResponseEntity<List<?>> filterByField(
            @PathVariable String databaseName,
            @RequestParam String collection,
            @RequestParam String field,
            @RequestParam String value) {
        try {
            // Vérifie si la base de données existe
            MongoDataBaseConfig.Database database = mongoDataBaseConfig.findDatabaseByName(databaseName);
            if (database == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Base de données non trouvée : " + databaseName);
            }

            // Filtre les données
            List<?> data = mongoDataBaseConfig.filterByField(databaseName, collection, field, value);
            return ResponseEntity.ok(data);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Collection ou champ invalide : " + e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Erreur lors du filtrage des données : " + e.getMessage());
        }
    }

    // Endpoint pour tester la connexion à une base de données
    @GetMapping("/testConnection/{databaseName}")
    public ResponseEntity<String> testConnection(@PathVariable String databaseName) {
        MongoDataBaseConfig.Database database = mongoDataBaseConfig.findDatabaseByName(databaseName);
        if (database == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Base de données non trouvée : " + databaseName);
        }

        boolean isConnected = mongoDataBaseConfig.testConnection(databaseName);
        if (isConnected) {
            return ResponseEntity.ok("Connexion à la base de données '" + databaseName + "' réussie.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Échec de la connexion à la base de données '" + databaseName + "'.");
        }
    }
}