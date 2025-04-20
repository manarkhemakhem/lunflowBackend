package com.example.lunflow.api;

import com.example.lunflow.DataBases.MongoDataBaseConfig;
import com.example.lunflow.Service.CollaboratorService;
import com.example.lunflow.dao.Model.Collaborator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*") // Pour Angular, consider restricting origins in production
@RestController
@RequestMapping("/api/{databaseName}/collaborators")
public class CollaboratorController {

    private final CollaboratorService collaboratorService;
    private final MongoDataBaseConfig mongoDataBaseConfig;

    @Autowired
    public CollaboratorController(CollaboratorService collaboratorService, MongoDataBaseConfig mongoDataBaseConfig) {
        this.collaboratorService = collaboratorService;
        this.mongoDataBaseConfig = mongoDataBaseConfig;
    }

    @GetMapping
    public ResponseEntity<List<Collaborator>> getAllCollaborators(@PathVariable String databaseName) {
        try {
            validateDatabaseName(databaseName);
            List<Collaborator> collaborators = collaboratorService.getAllCollaborators(databaseName);
            return ResponseEntity.ok(collaborators);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Collaborator> getCollaboratorById(@PathVariable String databaseName, @PathVariable String id) {
        try {
            validateDatabaseName(databaseName);
            Collaborator collaborator = collaboratorService.getCollaboratorById(databaseName, id);
            return collaborator != null ? ResponseEntity.ok(collaborator) : ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/histogram")
    public ResponseEntity<Map<String, Long>> getHistogram(@PathVariable String databaseName) {
        try {
            validateDatabaseName(databaseName);
            Map<String, Long> histogram = collaboratorService.getCreationDatesHistogram(databaseName);
            return ResponseEntity.ok(histogram);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/Admin")
    public ResponseEntity<List<Collaborator>> getAllAdmin(@PathVariable String databaseName) {
        try {
            validateDatabaseName(databaseName);
            List<Collaborator> admins = collaboratorService.getCollaboratorIsAdmin(databaseName);
            return ResponseEntity.ok(admins);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/NotAdmin")
    public ResponseEntity<List<Collaborator>> getAllNotAdmin(@PathVariable String databaseName) {
        try {
            validateDatabaseName(databaseName);
            List<Collaborator> nonAdmins = collaboratorService.getCollaboratorIsAdminFalse(databaseName);
            return ResponseEntity.ok(nonAdmins);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/onLine")
    public ResponseEntity<List<Collaborator>> getAllOnLine(@PathVariable String databaseName) {
        try {
            validateDatabaseName(databaseName);
            List<Collaborator> onlineCollaborators = collaboratorService.getCollaboratoronline(databaseName);
            return ResponseEntity.ok(onlineCollaborators);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/offLine")
    public ResponseEntity<List<Collaborator>> getAllOffLine(@PathVariable String databaseName) {
        try {
            validateDatabaseName(databaseName);
            List<Collaborator> offlineCollaborators = collaboratorService.getCollaboratoroffline(databaseName);
            return ResponseEntity.ok(offlineCollaborators);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<Collaborator>> findByGroupId(@PathVariable String databaseName, @PathVariable String groupId) {
        try {
            validateDatabaseName(databaseName);
            List<Collaborator> collaborators = collaboratorService.getCollaboratorByGroupId(databaseName, groupId);
            return ResponseEntity.ok(collaborators);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/{id}/deleted")
    public ResponseEntity<Boolean> getDeletedStatus(@PathVariable String databaseName, @PathVariable String id) {
        try {
            validateDatabaseName(databaseName);
            Boolean deletedStatus = collaboratorService.getCollaboratorDeletedStatus(databaseName, id);
            return deletedStatus != null ? ResponseEntity.ok(deletedStatus) : ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/fields")
    public ResponseEntity<List<String>> getCollaborateurFieldNames() {
        try {
            Field[] fields = Collaborator.class.getDeclaredFields();
            List<String> fieldNames = Arrays.stream(fields)
                    .map(Field::getName)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(fieldNames);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Collaborator>> search(@PathVariable String databaseName, @RequestParam String fullname) {
        try {
            validateDatabaseName(databaseName);
            List<Collaborator> collaborators = collaboratorService.searchByFullnameRegexIgnoreCase(databaseName, fullname);
            return ResponseEntity.ok(collaborators);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/count-by-field/{fieldName}")
    public ResponseEntity<Map<String, Long>> countByField(@PathVariable String databaseName, @PathVariable String fieldName) {
        try {
            validateDatabaseName(databaseName);
            Map<String, Long> counts = collaboratorService.countByField(databaseName, fieldName);
            return ResponseEntity.ok(counts);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    private void validateDatabaseName(String databaseName) {
        if (mongoDataBaseConfig.findDatabaseByName(databaseName) == null) {
            throw new IllegalArgumentException("Database not found: " + databaseName);
        }
    }
}