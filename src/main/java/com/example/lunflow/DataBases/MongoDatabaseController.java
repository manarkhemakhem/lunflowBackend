package com.example.lunflow.DataBases;

import com.example.lunflow.ValueType;
import com.example.lunflow.dao.Model.Collaborator;
import com.example.lunflow.dao.Model.Group;
import com.example.lunflow.dao.Model.User;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
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
    // Updated endpoint to get field names for any collection
    @GetMapping("/fields/{collectionName}")
    public ResponseEntity<List<String>> getFieldNames(@PathVariable String collectionName) {
        try {
            Class<?> clazz = getClassForCollection(collectionName);
            List<String> fieldNames = mongoDataBaseConfig.getFieldNames(clazz);
            return ResponseEntity.ok(fieldNames);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Collection invalide : " + collectionName);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Erreur lors de la récupération des champs : " + e.getMessage());
        }
    }

    // Endpoint pour filtrer les données par champ
    @GetMapping("/{databaseName}/filter")
    public ResponseEntity<List<?>> filterByField(
            @PathVariable String databaseName,
            @RequestParam String collection,
            @RequestParam String field,
            @RequestParam String operator,
            @RequestParam String value) {
        try {
            MongoDataBaseConfig.Database database = mongoDataBaseConfig.findDatabaseByName(databaseName);
            if (database == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Base de données non trouvée : " + databaseName);
            }

            Class<?> clazz = getClassForCollection(collection);
            System.out.println("Classe déterminée pour la collection '" + collection + "' : " + clazz.getName());

            Field fieldObj = clazz.getDeclaredField(field);
            System.out.println("Champ recherché : " + field);

            Class<?> fieldType = fieldObj.getType();
            System.out.println("Type détecté pour le champ '" + field + "' : " + fieldType.getName());

            System.out.println("Valeur fournie : " + value);
            System.out.println("Opérateur choisi : " + operator);

            ValueType valueType = mongoDataBaseConfig.createValueType(value, fieldType, operator);
            System.out.println("ValueType créé : " + valueType);

            System.out.println("Base de données sélectionnée : " + databaseName);
            System.out.println("Collection ciblée : " + collection);

            List<?> data = mongoDataBaseConfig.filterByValueType(databaseName, collection, field, operator, valueType);
            System.out.println("Données filtrées retournées : " + data);

            return ResponseEntity.ok(data);
        } catch (NoSuchFieldException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Champ invalide : " + field);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Collection, champ ou valeur invalide : " + e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Erreur lors du filtrage des données : " + e.getMessage());
        }
    }


    private Class<?> getClassForCollection(String collectionName) {
        return switch (collectionName.toLowerCase()) {
            case "collaborator" -> Collaborator.class;
            case "user" -> User.class;
            case "group" -> Group.class;
            default -> throw new IllegalArgumentException("Collection inconnue : " + collectionName);
        };
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