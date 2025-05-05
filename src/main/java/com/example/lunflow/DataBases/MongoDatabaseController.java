package com.example.lunflow.DataBases;

import com.example.lunflow.ValueType;
import com.example.lunflow.dao.Model.Collaborator;
import com.example.lunflow.dao.Model.Group;
import com.example.lunflow.dao.Model.User;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

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
            @RequestParam String field ,
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


    //**************************
    // retourn les fields  celon la base souhaiter et la collection
    @GetMapping("/fields")
    public ResponseEntity<List<String>> getFields(
            @RequestParam String databaseName,
            @RequestParam String collection) {
        try {
            List<String> fields = mongoDataBaseConfig.getFields(databaseName, collection);
            return ResponseEntity.ok(fields);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Erreur lors de la récupération des champs : " + e.getMessage());
        }
    }
    @GetMapping("/filter-field")
    public ResponseEntity<List<Object>> filterFieldValues(
            @RequestParam String databaseName,
            @RequestParam String collection,
            @RequestParam String field,
            @RequestParam(defaultValue = "false") boolean filter,
            @RequestParam(required = false) String operator,
            @RequestParam(required = false) String value) {
        try {
            MongoTemplate mongoTemplate = mongoDataBaseConfig.getMongoTemplateForDatabase(databaseName);

            if (!filter) {
                Query query = new Query();
                query.fields().include(field);
                List<Map> results = mongoTemplate.find(query, Map.class, collection);
                List<Object> fieldValues = results.stream()
                        .map(m -> m.get(field))
                        .filter(Objects::nonNull)
                        .toList();
                return ResponseEntity.ok(fieldValues);
            }

            if (operator == null || value == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Opérateur et valeur requis si filter=true");
            }

            // Déduire dynamiquement le type du champ depuis un document existant
            Object sample = mongoTemplate.findOne(new Query(), Map.class, collection);
            if (sample == null || !((Map<?, ?>) sample).containsKey(field)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Champ inconnu ou collection vide : " + field);
            }

            Object exampleValue = ((Map<?, ?>) sample).get(field);
            Object typedValue = convertToTypedValue(value, exampleValue);

            Criteria criteria = switch (operator.toLowerCase()) {
                case "eq" -> Criteria.where(field).is(typedValue);
                case "ne" -> Criteria.where(field).ne(typedValue);
                case "regex" -> Criteria.where(field).regex(value, "i");
                case "in" -> {
                    List<Object> values = Arrays.stream(value.split(","))
                            .map(v -> convertToTypedValue(v, exampleValue))
                            .toList();
                    yield Criteria.where(field).in(values);
                }
                default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Opérateur non supporté");
            };

            Query query = new Query(criteria);
            query.fields().include(field);
            List<Map> results = mongoTemplate.find(query, Map.class, collection);
            List<Object> filteredFieldValues = results.stream()
                    .map(m -> m.get(field))
                    .filter(Objects::nonNull)
                    .toList();

            return ResponseEntity.ok(filteredFieldValues);

        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Erreur interne : " + e.getMessage());
        }
    }

    // Méthode utilitaire pour convertir dynamiquement la valeur
    private Object convertToTypedValue(String value, Object example) {
        if (example instanceof Integer) {
            return Integer.parseInt(value);
        } else if (example instanceof Long) {
            return Long.parseLong(value);
        } else if (example instanceof Boolean) {
            return Boolean.parseBoolean(value);
        } else if (example instanceof Date) {
            try {
                return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").parse(value);
            } catch (ParseException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Format de date invalide : " + value);
            }
        }
        return value; // fallback String
    }


}