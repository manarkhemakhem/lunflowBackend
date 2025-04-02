package com.example.lunflow.Service;

import com.example.lunflow.dao.Model.Collaborator;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface CollaboratorService {
    Map<String, Long>getCreationDatesHistogram();
    List<Collaborator> getAllCollaborators();
     Boolean getCollaboratorDeletedStatus(String collaboratorId);
    List<Collaborator> getCollaboratoroffline() ;
    List<Collaborator> getCollaboratoronline();
    Collaborator getCollaboratorById(String id);
    List<Collaborator>getCollaboratorByGroupId(String groupId);
    List<Collaborator> getCollaboratorIsAdmin();
    List<Collaborator> getCollaboratorIsAdminFalse();

    List<Collaborator> searchByFullnameRegexIgnoreCase(String fullname);

}
