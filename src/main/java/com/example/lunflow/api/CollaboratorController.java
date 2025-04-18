package com.example.lunflow.api;

import com.example.lunflow.Service.CollaboratorService;
import com.example.lunflow.dao.Model.Collaborator;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")// Pour Angular
@RestController
@RequestMapping("/api/{databaseName}/collaborators")
public class CollaboratorController {
    @Autowired
    private final CollaboratorService collaboratorService;

    public CollaboratorController(CollaboratorService collaboratorService) {
        this.collaboratorService = collaboratorService;
    }

    @GetMapping
    public List<Collaborator> getAllCollaborators() {
        return collaboratorService.getAllCollaborators();
    }

    @GetMapping("/{id}")
    public Collaborator getCollaboratorById(@PathVariable String id) {
        return collaboratorService.getCollaboratorById(id);
    }
    @GetMapping("/histogram")
    public Map<String, Long> getHistogram() {
        return collaboratorService.getCreationDatesHistogram();
    }



    @GetMapping("/Admin")
    public List<Collaborator> getAllAdmin() {
        return collaboratorService.getCollaboratorIsAdmin();
    }@GetMapping("/NotAdmin")
    public List<Collaborator> getAllNotAdmin() {
        return collaboratorService.getCollaboratorIsAdminFalse();
    }


    @GetMapping("/onLine")
    public List<Collaborator> getAllOnLine() {
        return collaboratorService.getCollaboratoronline();
    }
    @GetMapping("/offLine")
    public List<Collaborator> getAllOffLine() {
        return collaboratorService.getCollaboratoroffline();
    }

    @GetMapping("/group/{groupId}")
    public List<Collaborator> findByGroupId (@PathVariable String groupId) {
        return collaboratorService.getCollaboratorByGroupId(groupId);
    }
    @GetMapping("/{id}/deleted")
    public ResponseEntity<Boolean> getDeletedStatus(@PathVariable String id) {
        Boolean deletedStatus = collaboratorService.getCollaboratorDeletedStatus(id);
        return ResponseEntity.ok(deletedStatus);
    }

    @GetMapping("/fields")
    public List<String> getCollaborateurFieldNames() {
        Field[] fields = Collaborator.class.getDeclaredFields();
        return Arrays.stream(fields)
                .map(Field::getName)
                .collect(Collectors.toList());
    }

@GetMapping("/search")
public List<Collaborator> search(@RequestParam String fullname) {
    // Appel au service qui interroge le repository
    return collaboratorService.searchByFullnameRegexIgnoreCase(fullname);
    }
    @GetMapping("/count-by-field/{fieldName}")
    public Map<String, Long> countByField(@PathVariable String fieldName) {
        return collaboratorService.countByField(fieldName);
    }
}