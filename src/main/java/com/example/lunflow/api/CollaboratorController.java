package com.example.lunflow.api;

import com.example.lunflow.dao.Model.Collaborator;
import com.example.lunflow.Service.Impl.CollaboratorServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")// Pour Angular
@RestController
@RequestMapping("/api/collaborators")
public class CollaboratorController {
    @Autowired
    private CollaboratorServiceImpl collaboratorService;

    @GetMapping
    public List<Collaborator> getAllCollaborators() {
        return collaboratorService.getAllCollaborators();
    }

    @GetMapping("/{id}")
    public Collaborator getCollaboratorById(@PathVariable String id) {
        return collaboratorService.getCollaboratorById(id);
    }



    @DeleteMapping("/{id}")
    public void deleteCollaborator(@PathVariable String id) {
        collaboratorService.deleteCollaborator(id);
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