package com.example.lunflow.Service;

import com.example.lunflow.dao.Model.Collaborator;
import com.example.lunflow.dao.Model.Group;

import java.util.List;

public interface GroupService {

    List<Group> getAllGroups();
    Group getGroupById(String id);
    List<String>  getCollaboratorList(String groupId);
    List<Collaborator>  getCollaboratorDetails(List<String> collabIds);
    Integer getNbWrkflowtypeById(String id);


}
