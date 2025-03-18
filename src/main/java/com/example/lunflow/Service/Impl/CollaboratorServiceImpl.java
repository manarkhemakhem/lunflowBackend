package com.example.lunflow.Service.Impl;

import com.example.lunflow.Service.CollaboratorService;
import com.example.lunflow.dao.CollaboratorDao;
import com.example.lunflow.dao.Model.Collaborator;
import com.example.lunflow.dao.Repository.CollaboratorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service

public class CollaboratorServiceImpl  implements CollaboratorService {
    @Autowired
    private CollaboratorDao collaboratorDao;


    public List<Collaborator> getAllCollaborators() {
        return collaboratorDao.findAll();
    }

    public Collaborator getCollaboratorById(String id) {
        return collaboratorDao.findById(id);
    }

    @Override
    public List<Collaborator> getCollaboratorByGroupId(String groupId) {
        return collaboratorDao.findByGroupId(groupId);
    }

    public List<Collaborator> getCollaboratorIsAdmin () {
        return collaboratorDao.findByIsAdmin(true);
    }

    public List<Collaborator> getCollaboratorIsAdminFalse() {
        return collaboratorDao.findByIsAdminFalse(false);
    }

    public void deleteCollaborator(String id) {
        collaboratorDao.deleteById(id);
    }
}
