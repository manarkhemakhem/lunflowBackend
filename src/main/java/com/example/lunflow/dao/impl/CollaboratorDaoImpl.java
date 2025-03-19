package com.example.lunflow.dao.impl;

import com.example.lunflow.dao.CollaboratorDao;
import com.example.lunflow.dao.Model.Collaborator;
import com.example.lunflow.dao.Repository.CollaboratorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class CollaboratorDaoImpl implements CollaboratorDao {
    private  final CollaboratorRepo collaboratorRepo ;
@Autowired
    public CollaboratorDaoImpl(CollaboratorRepo collaboratorRepo) {
        this.collaboratorRepo = collaboratorRepo;
    }

    @Override
    public List<Collaborator> findAll() {
        return collaboratorRepo.findAll();
    }

    @Override
    public Collaborator findById(String id) {
        return collaboratorRepo.findById(id).orElse(null);
    }

    @Override
    public List<Collaborator> findByGroupId(String groupId) {
        return collaboratorRepo.findByGroupId(groupId);
    }

    @Override
    public List<Collaborator> findByIsAdmin(boolean isAdmin) {
        return collaboratorRepo.findByIsAdmin(isAdmin);
    }

    @Override
    public List<Collaborator> findByIsAdminFalse(boolean isAdmin) {
        return collaboratorRepo.findByIsAdminFalse(isAdmin);
    }

    @Override
    public void deleteById(String id) {
    collaboratorRepo.deleteById(id);

    }
}
