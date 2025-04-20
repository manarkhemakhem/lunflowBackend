package com.example.lunflow.Service;

import com.example.lunflow.dao.Model.Collaborator;

import java.util.List;
import java.util.Map;

public interface CollaboratorService {
    List<Collaborator> getAllCollaborators(String databaseName);
    Collaborator getCollaboratorById(String databaseName, String id);
    List<Collaborator> getCollaboratorByGroupId(String databaseName, String groupId);
    Map<String, Long> getCreationDatesHistogram(String databaseName);
    Map<String, Long> countByField(String databaseName, String fieldName);
    List<Collaborator> getCollaboratorIsAdmin(String databaseName);
    List<Collaborator> getCollaboratorIsAdminFalse(String databaseName);
    List<Collaborator> getCollaboratoronline(String databaseName);
    List<Collaborator> getCollaboratoroffline(String databaseName);
    Boolean getCollaboratorDeletedStatus(String databaseName, String collaboratorId);
    List<Collaborator> searchByFullnameRegexIgnoreCase(String databaseName, String fullname);
}