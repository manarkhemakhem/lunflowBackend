package com.example.lunflow.Service;

import com.example.lunflow.dao.Model.Collaborator;
import com.example.lunflow.dao.Model.Group;
import java.util.List;

public interface GroupService {
    List<Group> getAllGroups(String databaseName);
    Group getGroupById(String databaseName, String id);
    List<String> getCollaboratorList(String databaseName, String groupId);
    List<Collaborator> getCollaboratorDetails(String databaseName, List<String> collabIds);
    Integer getNbWrkflowtypeById(String databaseName, String id);
}