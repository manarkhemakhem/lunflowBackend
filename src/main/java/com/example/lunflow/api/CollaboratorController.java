package com.example.lunflow.api;

import com.example.lunflow.Service.CollaboratorService;
import com.example.lunflow.dao.Model.Collaborator;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")// Pour Angular
@RestController
@RequestMapping("/api/collaborators")
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
    }

    @GetMapping("/NotAdmin")
    public List<Collaborator> getAllNotAdmin() {
        return collaboratorService.getCollaboratorIsAdminFalse();
    }

    @GetMapping("/group/{groupId}")
    public List<Collaborator> findByGroupId (@PathVariable String groupId) {
        return collaboratorService.getCollaboratorByGroupId(groupId);
    }
}