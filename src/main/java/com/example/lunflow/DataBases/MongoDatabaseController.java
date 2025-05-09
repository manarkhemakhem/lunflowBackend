package com.example.lunflow.DataBases;

import com.example.lunflow.Filter;
import com.example.lunflow.FilterRequest;
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
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
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

    @PostMapping("/{databaseName}/{collection}/filter")
    public ResponseEntity<List<?>> filterByFields(
            @PathVariable String databaseName,
            @PathVariable String collection,
            @RequestBody List<Map<String, String>> filters) {
        try {
            MongoDataBaseConfig.Database database = mongoDataBaseConfig.findDatabaseByName(databaseName);
            if (database == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Base de données non trouvée : " + databaseName);
            }

            MongoTemplate mongoTemplate = mongoDataBaseConfig.getMongoTemplateForDatabase(databaseName);
            Class<?> clazz = getClassForCollection(collection);

            List<Criteria> criteriaList = new ArrayList<>();
            for (Map<String, String> filter : filters) {
                String field = filter.get("field");
                String operator = filter.get("operator");
                String value = filter.get("value");

                Field fieldObj = clazz.getDeclaredField(field);
                Class<?> fieldType = fieldObj.getType();
                ValueType valueType = mongoDataBaseConfig.createValueType(value, fieldType, operator);

                Criteria criteria;
                if (valueType.getStringValue() != null) {
                    String val = valueType.getStringValue();
                    criteria = switch (operator.toLowerCase()) {
                        case "equals" -> Criteria.where(field).is(val);
                        case "notequals" -> Criteria.where(field).ne(val);
                        case "contains" -> Criteria.where(field).regex(val, "i");
                        case "notcontains" -> Criteria.where(field).not().regex(val, "i");
                        case "startswith" -> Criteria.where(field).regex("^" + val, "i");
                        case "endswith" -> Criteria.where(field).regex(val + "$", "i");
                        case "regex" -> Criteria.where(field).regex(val, "i");
                        default -> throw new IllegalArgumentException("Opérateur non supporté pour String : " + operator);
                    };
                } else if (valueType.getBoolValue() != null) {
                    Boolean val = valueType.getBoolValue();
                    criteria = switch (operator.toLowerCase()) {
                        case "equals" -> Criteria.where(field).is(val);
                        case "notequals" -> Criteria.where(field).ne(val);
                        default -> throw new IllegalArgumentException("Opérateur non supporté pour Boolean : " + operator);
                    };
                } else if (valueType.getIntValue() != null) {
                    Integer val = valueType.getIntValue();
                    criteria = switch (operator.toLowerCase()) {
                        case "equals" -> Criteria.where(field).is(val);
                        case "notequals" -> Criteria.where(field).ne(val);
                        case "greaterthan" -> Criteria.where(field).gt(val);
                        case "lessthan" -> Criteria.where(field).lt(val);
                        default -> throw new IllegalArgumentException("Opérateur non supporté pour Integer : " + operator);
                    };
                } else if (valueType.getInstantValue() != null) {
                    Instant val = valueType.getInstantValue();
                    criteria = switch (operator.toLowerCase()) {
                        case "equals" -> {
                            ZonedDateTime start = val.atZone(ZoneOffset.UTC).truncatedTo(ChronoUnit.DAYS);
                            ZonedDateTime end = start.plusDays(1);
                            yield Criteria.where(field)
                                    .gte(Date.from(start.toInstant()))
                                    .lt(Date.from(end.toInstant()));
                        }
                        case "greaterthan" -> Criteria.where(field).gt(Date.from(val));
                        case "lessthan" -> Criteria.where(field).lt(Date.from(val));
                        case "dateafter" -> Criteria.where(field).gte(Date.from(val));
                        case "equalsyear" -> {
                            ZonedDateTime start = val.atZone(ZoneOffset.UTC).withDayOfYear(1).truncatedTo(ChronoUnit.DAYS);
                            ZonedDateTime end = start.plusYears(1);
                            yield Criteria.where(field)
                                    .gte(Date.from(start.toInstant()))
                                    .lt(Date.from(end.toInstant()));
                        }
                        default -> throw new IllegalArgumentException("Opérateur non supporté pour Instant : " + operator);
                    };
                } else {
                    throw new IllegalArgumentException("Aucune valeur détectée dans ValueType.");
                }
                criteriaList.add(criteria);
            }

            Query query = new Query(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
            List<Map> results = mongoTemplate.find(query, Map.class, collection);
            return ResponseEntity.ok(results);
        } catch (NoSuchFieldException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Champ invalide : " + e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Collection, champ ou valeur invalide : " + e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Erreur lors du filtrage des données : " + e.getMessage());
        }
    }

    private ResponseEntity<List<Object>> getFilteredResults(MongoTemplate mongoTemplate, String collection, String field, Criteria criteria) {
        Query query = new Query(criteria);
        query.fields().include(field);
        List<Map> results = mongoTemplate.find(query, Map.class, collection);
        List<Object> filteredFieldValues = results.stream()
                .map(m -> m.get(field))
                .filter(Objects::nonNull)
                .toList();
        return ResponseEntity.ok(filteredFieldValues);
    }

    private Object convertToTypedValue(String value, Object exampleValue) {
        try {
            if (exampleValue instanceof String) {
                return value;
            } else if (exampleValue instanceof Integer) {
                return Integer.parseInt(value);
            } else if (exampleValue instanceof Double) {
                return Double.parseDouble(value);
            } else if (exampleValue instanceof Boolean) {
                return Boolean.parseBoolean(value);
            } else if (exampleValue instanceof Date) {
                return new SimpleDateFormat("yyyy-MM-dd").parse(value);
            }
            return value; // Fallback si le type n'est pas reconnu
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Valeur invalide pour le type du champ : " + value);
        }
    }
    @GetMapping("/{databaseName}/{collection}/field-values/{field}")
    public ResponseEntity<List<Object>> getFieldValues(
            @PathVariable String databaseName,
            @PathVariable String collection,
            @PathVariable String field) {
        try {
            MongoTemplate mongoTemplate = mongoDataBaseConfig.getMongoTemplateForDatabase(databaseName);
            // Use findDistinct with Query to retrieve distinct values
            Query query = new Query();
            List<Object> values = mongoTemplate.findDistinct(query, field, collection, Object.class);
            return ResponseEntity.ok(values.stream().filter(Objects::nonNull).toList());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Erreur lors de la récupération des valeurs du champ : " + e.getMessage());
        }
    }
}