package com.example.lunflow.dao.impl;

import com.example.lunflow.dao.GroupDao;
import com.example.lunflow.dao.Model.Collaborator;
import com.example.lunflow.dao.Model.Group;
import com.example.lunflow.dao.Repository.GroupRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class GroupDaoImpl implements GroupDao {
     private final GroupRepo groupRepo;
@Autowired
    public GroupDaoImpl(GroupRepo groupRepo) {
        this.groupRepo = groupRepo;
    }


    @Override
    public List<Group> findAll() {
        return groupRepo.findAll();
    }

    @Override
    public Group findById(String id) {
        return groupRepo.findById(id).orElse(null);
    }

   @Override
    public List<Collaborator> findCollabList(List<String> collabIdList) {
       return groupRepo.findByCollabIdListIn(collabIdList);
    }



}
