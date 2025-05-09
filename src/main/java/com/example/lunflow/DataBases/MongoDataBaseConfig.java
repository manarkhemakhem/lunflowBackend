package com.example.lunflow.DataBases;
import com.example.lunflow.ValueType;
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
import java.lang.reflect.Field;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

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
    public List<String> getFields(String databaseName, String collection) {
        MongoTemplate mongoTemplate = getMongoTemplateForDatabase(databaseName);

        // Récupère quelques documents (ex: 100 max) pour explorer les champs
        Query query = new Query().limit(100);
        List<Map> documents = mongoTemplate.find(query, Map.class, collection);

        // Set pour éviter les doublons de champs
        Set<String> fieldNames = new HashSet<>();

        for (Map doc : documents) {
            fieldNames.addAll(doc.keySet());
        }

        return new ArrayList<>(fieldNames);
    }

    public List<?> filterByValueType(String databaseName, String collection, String field, String operator, ValueType valueType) {
        MongoTemplate mongoTemplate = getMongoTemplateForDatabase(databaseName);

        Criteria criteria;

        if (valueType.getStringValue() != null) {
            String val = valueType.getStringValue();
            switch (operator.toLowerCase()) {
                case "equals" -> criteria = Criteria.where(field).is(val);
                case "notequals" -> criteria = Criteria.where(field).ne(val);
                case "contains" -> criteria = Criteria.where(field).regex(val, "i");
                case "notcontains" -> criteria = Criteria.where(field).not().regex(val, "i");
                case "startswith" -> criteria = Criteria.where(field).regex("^" + val, "i");
                case "endswith" -> criteria = Criteria.where(field).regex(val + "$", "i");
                default -> throw new IllegalArgumentException("Opérateur non supporté pour String : " + operator);
            }

        } else if (valueType.getBoolValue() != null) {
            Boolean val = valueType.getBoolValue();
            switch (operator.toLowerCase()) {
                case "equals" -> criteria = Criteria.where(field).is(val);
                case "notequals" -> criteria = Criteria.where(field).ne(val);
                default -> throw new IllegalArgumentException("Opérateur non supporté pour Boolean : " + operator);
            }

        } else if (valueType.getIntValue() != null) {
            Integer val = valueType.getIntValue();
            switch (operator.toLowerCase()) {
                case "equals" -> criteria = Criteria.where(field).is(val);
                case "notequals" -> criteria = Criteria.where(field).ne(val);
                default -> throw new IllegalArgumentException("Opérateur non supporté pour Integer : " + operator);
            }

        } else if (valueType.getInstantValue() != null) {
            Instant val = valueType.getInstantValue();
            switch (operator.toLowerCase()) {
                case "equals" -> {
                    ZonedDateTime start = val.atZone(ZoneOffset.UTC).truncatedTo(ChronoUnit.DAYS);
                    ZonedDateTime end = start.plusDays(1);
                    criteria = Criteria.where(field)
                            .gte(Date.from(start.toInstant()))
                            .lt(Date.from(end.toInstant()));
                }
                case "greaterthan" -> criteria = Criteria.where(field).gt(Date.from(val));
                case "lessthan" -> criteria = Criteria.where(field).lt(Date.from(val));
                case "equalsyear" -> {
                    ZonedDateTime start = val.atZone(ZoneOffset.UTC).withDayOfYear(1).truncatedTo(ChronoUnit.DAYS);
                    ZonedDateTime end = start.plusYears(1);
                    criteria = Criteria.where(field)
                            .gte(Date.from(start.toInstant()))
                            .lt(Date.from(end.toInstant()));
                }
                default -> throw new IllegalArgumentException("Opérateur non supporté pour Instant : " + operator);
            }

        } else {
            throw new IllegalArgumentException("Aucune valeur détectée dans ValueType.");
        }

        Query query = new Query(criteria);
        return mongoTemplate.find(query, Map.class, collection);
    }


    public List<String> getFieldNames(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        return Arrays.stream(fields)
                .map(Field::getName)
                .collect(Collectors.toList());
    }
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
    public ValueType createValueType(String value, Class<?> fieldType, String operator) {
        ValueType valueType = new ValueType();

        if (fieldType == String.class) {
            if (!List.of("equals", "notequals", "contains", "notcontains", "startswith", "endswith", "regex")
                    .contains(operator.toLowerCase())) {
                throw new IllegalArgumentException("Opérateur non supporté pour String : " + operator);
            }
            valueType.setStringValue(value);

        } else if (fieldType == Boolean.class || fieldType == boolean.class) {
            if (!List.of("equals", "notequals").contains(operator.toLowerCase())) {
                throw new IllegalArgumentException("Opérateur non supporté pour Boolean : " + operator);
            }
            valueType.setBoolValue(Boolean.parseBoolean(value));

        } else if (fieldType == Integer.class || fieldType == int.class) {
            if (!List.of("equals", "notequals", "greaterthan", "lessthan").contains(operator.toLowerCase())) {
                throw new IllegalArgumentException("Opérateur non supporté pour Integer : " + operator);
            }
            try {
                valueType.setIntValue(Integer.parseInt(value));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Valeur invalide pour Integer : " + value);
            }

        } else if (fieldType == LocalDateTime.class || fieldType == Instant.class || fieldType == Date.class) {
            if (!List.of("equals", "notequals", "greaterthan", "lessthan", "equalsyear", "dateafter")
                    .contains(operator.toLowerCase())) {
                throw new IllegalArgumentException("Opérateur non supporté pour Date : " + operator);
            }
            try {
                if (operator.equalsIgnoreCase("equalsyear")) {
                    int year = Integer.parseInt(value);
                    LocalDateTime start = LocalDateTime.of(year, 1, 1, 0, 0);
                    valueType.setDateValue(start);
                    valueType.setInstantValue(start.atZone(ZoneOffset.UTC).toInstant());
                } else {
                    LocalDateTime parsedDate = parseLocalDateTime(value); // à adapter si nécessaire
                    valueType.setDateValue(parsedDate);
                    valueType.setInstantValue(parsedDate.atZone(ZoneOffset.UTC).toInstant());
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("Valeur invalide pour Date/Instant : " + value);
            }
        } else {
            throw new IllegalArgumentException("Type de champ non supporté : " + fieldType.getSimpleName());
        }

        return valueType;
    }


    private LocalDateTime parseLocalDateTime(String value) throws DateTimeParseException {
        LocalDateTime parsedDate;

        if (value.matches("\\d{4}")) { // Si c'est juste une année (ex: 2024)
            parsedDate = LocalDateTime.parse(value + "-01-01T00:00:00");
        } else if (value.matches("\\d{4}-\\d{2}-\\d{2}")) { // Si c'est une date sans heure (ex: 2024-04-23)
            parsedDate = LocalDate.parse(value).atStartOfDay();
        } else if (value.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}")) { // Si c'est une date avec heure sans seconde (ex: 2024-04-23T10:30)
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            parsedDate = LocalDateTime.parse(value, formatter);
        } else { // Si c'est au format ISO (ex: 2024-04-23T10:30:00)
            parsedDate = LocalDateTime.parse(value, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }

        return parsedDate;
    }

}