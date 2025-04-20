package com.example.lunflow.Service.Impl;

import com.example.lunflow.DataBases.MongoDataBaseConfig;
import com.example.lunflow.Service.GroupService;
import com.example.lunflow.dao.Model.Collaborator;
import com.example.lunflow.dao.Model.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class GroupServiceImpl implements GroupService {
    private final MongoDataBaseConfig mongoDataBaseConfig;

    @Autowired
    public GroupServiceImpl(MongoDataBaseConfig mongoDataBaseConfig) {
        this.mongoDataBaseConfig = mongoDataBaseConfig;
    }

    @Override
    public List<Group> getAllGroups(String databaseName) {
        Objects.requireNonNull(databaseName, "Database name cannot be null");
        MongoTemplate mongoTemplate = mongoDataBaseConfig.getMongoTemplateForDatabase(databaseName);
        List<Group> groups = mongoTemplate.findAll(Group.class, "group");
        System.out.println("GroupServiceImpl: Fetched " + groups.size() + " groups from database '" + databaseName + "'");
        return groups;
    }

    @Override
    public Group getGroupById(String databaseName, String id) {
        Objects.requireNonNull(databaseName, "Database name cannot be null");
        Objects.requireNonNull(id, "Group ID cannot be null");
        MongoTemplate mongoTemplate = mongoDataBaseConfig.getMongoTemplateForDatabase(databaseName);
        Group group = mongoTemplate.findById(id, Group.class, "group");
        System.out.println("GroupServiceImpl: Fetched group ID '" + id + "' from database '" + databaseName + "': " + (group != null ? group.getLabel() : "null"));
        return group;
    }

    @Override
    public List<String> getCollaboratorList(String databaseName, String groupId) {
        Objects.requireNonNull(databaseName, "Database name cannot be null");
        Objects.requireNonNull(groupId, "Group ID cannot be null");
        Group group = getGroupById(databaseName, groupId);
        List<String> collabIds = group != null ? group.getCollabIdList() : new ArrayList<>();
        System.out.println("GroupServiceImpl: Fetched " + collabIds.size() + " collaborator IDs for group ID '" + groupId + "' from database '" + databaseName + "'");
        return collabIds;
    }

    @Override
    public List<Collaborator> getCollaboratorDetails(String databaseName, List<String> collabIds) {
        Objects.requireNonNull(databaseName, "Database name cannot be null");
        Objects.requireNonNull(collabIds, "Collaborator IDs cannot be null");
        MongoTemplate mongoTemplate = mongoDataBaseConfig.getMongoTemplateForDatabase(databaseName);
        List<Collaborator> collaborators = new ArrayList<>();
        for (String id : collabIds) {
            Collaborator collaborator = mongoTemplate.findById(id, Collaborator.class, "collaborator");
            if (collaborator != null) {
                collaborators.add(collaborator);
            }
        }
        System.out.println("GroupServiceImpl: Fetched " + collaborators.size() + " collaborators from database '" + databaseName + "'");
        return collaborators;
    }

    @Override
    public Integer getNbWrkflowtypeById(String databaseName, String id) {
        Objects.requireNonNull(databaseName, "Database name cannot be null");
        Objects.requireNonNull(id, "Group ID cannot be null");
        Group group = getGroupById(databaseName, id);
        Integer nbWrkftype = group != null ? group.getNbWrkftype() : null;
        System.out.println("GroupServiceImpl: Fetched nbWrkftype=" + nbWrkftype + " for group ID '" + id + "' from database '" + databaseName + "'");
        return nbWrkftype;
    }
}