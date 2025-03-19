package com.example.lunflow.api;

import com.example.lunflow.Service.GroupService;
import com.example.lunflow.Service.Impl.CollaboratorServiceImpl;
import com.example.lunflow.Service.Impl.GroupServiceImpl;
import com.example.lunflow.dao.Model.Collaborator;
import com.example.lunflow.dao.Model.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    //Endpoint pour obtenir les collaborateurs d'un groupe par une liste d'IDs de collaborateurs
    @GetMapping("/{groupId}/collaborators")
    public List<Collaborator> getCollaboratorList(@PathVariable String groupId) {
        // Récupérer le groupe à partir de son ID
        Group group = groupService.getGroupById(groupId);

        if (group == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Group not found");
        }

        // Récupérer les collaborateurs à partir de la liste des IDs dans le groupe
        List<String> collabIdList = group.getCollabIdList(); // Liste des IDs des collaborateurs du groupe
        return groupService.getCollaboratorList(collabIdList);  // Retourner les collaborateurs
    }
}


