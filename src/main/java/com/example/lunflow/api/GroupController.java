package com.example.lunflow.api;

import com.example.lunflow.Service.GroupService;
import com.example.lunflow.Service.Impl.CollaboratorServiceImpl;
import com.example.lunflow.Service.Impl.GroupServiceImpl;
import com.example.lunflow.dao.Model.Collaborator;
import com.example.lunflow.dao.Model.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@CrossOrigin(origins = "*")// Pour Angular
@RestController
@RequestMapping("/api/groups")
public class GroupController {
    private final GroupService groupService;

    @Autowired

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping("/all")
    public List<Group> getAllGroups() {
        return groupService.getAllGroups();
    }

    // Endpoint pour obtenir un groupe par son ID
    @GetMapping("/{id}")
    public Group getGroupById(@PathVariable String id) {
        return groupService.getGroupById(id);
    }

    @GetMapping("/{groupId}/collaborators")
    public ResponseEntity<List<Collaborator>> getCollaboratorList(@PathVariable String groupId) {
        // Étape 1 : Récupérer les IDs des collaborateurs du groupe
        List<String> collabIds = groupService.getCollaboratorList(groupId);

        if (collabIds != null && !collabIds.isEmpty()) {
            // Étape 2 : Récupérer les détails des collaborateurs en fonction des IDs
            List<Collaborator> collaborators = groupService.getCollaboratorDetails(collabIds);
            return ResponseEntity.ok(collaborators);
        }

        return ResponseEntity.notFound().build();
    }
    @GetMapping("/{id}/nbwrkftype")
    public Integer getNbWrkflowtypeByGroupId(@PathVariable String id) {
        Integer nbWrkftype = groupService.getNbWrkflowtypeById(id);
        if (nbWrkftype == null) {
            return 0;
        }

        return nbWrkftype;
    }

}


