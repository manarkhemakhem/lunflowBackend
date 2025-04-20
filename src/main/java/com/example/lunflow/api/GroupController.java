package com.example.lunflow.api;

import com.example.lunflow.Service.GroupService;
import com.example.lunflow.dao.Model.Collaborator;
import com.example.lunflow.dao.Model.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*") // Pour Angular
@RestController
@RequestMapping("/api/{databaseName}/groups")
public class GroupController {
    private final GroupService groupService;

    @Autowired
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping("/all")
    public List<Group> getAllGroups(@PathVariable String databaseName) {
        return groupService.getAllGroups(databaseName);
    }

    @GetMapping("/{id}")
    public Group getGroupById(@PathVariable String databaseName, @PathVariable String id) {
        return groupService.getGroupById(databaseName, id);
    }

    @GetMapping("/{groupId}/collaborators")
    public ResponseEntity<List<Collaborator>> getCollaboratorList(@PathVariable String databaseName, @PathVariable String groupId) {
        List<String> collabIds = groupService.getCollaboratorList(databaseName, groupId);
        if (collabIds != null && !collabIds.isEmpty()) {
            List<Collaborator> collaborators = groupService.getCollaboratorDetails(databaseName, collabIds);
            return ResponseEntity.ok(collaborators);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/nbwrkftype")
    public Integer getNbWrkflowtypeByGroupId(@PathVariable String databaseName, @PathVariable String id) {
        Integer nbWrkftype = groupService.getNbWrkflowtypeById(databaseName, id);
        return nbWrkftype != null ? nbWrkftype : 0;
    }
}