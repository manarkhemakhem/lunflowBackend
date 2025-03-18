package com.example.lunflow.Service;

import com.example.lunflow.dao.Model.Collaborator;

import java.util.List;

public interface CollaboratorService {

    List<Collaborator> getAllCollaborators();

    Collaborator getCollaboratorById(String id);
    List<Collaborator>getCollaboratorByGroupId(String groupId);
    List<Collaborator> getCollaboratorIsAdmin();
    List<Collaborator> getCollaboratorIsAdminFalse();


    void deleteCollaborator(String id);
}
