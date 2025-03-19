package com.example.lunflow.Service.Impl;

import com.example.lunflow.Service.GroupService;
import com.example.lunflow.dao.CollaboratorDao;
import com.example.lunflow.dao.GroupDao;
import com.example.lunflow.dao.Model.Collaborator;
import com.example.lunflow.dao.Model.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class GroupServiceImpl  implements GroupService {

    private final GroupDao groupDao;
    @Autowired

    public GroupServiceImpl(GroupDao groupDao) {
        this.groupDao = groupDao;
    }


    @Override
    public List<Group> getAllGroups() {
        return groupDao.findAll();
    }

    @Override
    public Group getGroupById(String id) {
        return groupDao.findById(id);
    }

   @Override
 public List<Collaborator> getCollaboratorList(List<String> collabIdList) {
       return groupDao.findCollabList(collabIdList);
  }
}
