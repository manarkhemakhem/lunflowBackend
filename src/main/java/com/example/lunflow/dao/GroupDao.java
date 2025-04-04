package com.example.lunflow.dao;

import com.example.lunflow.dao.Model.Collaborator;
import com.example.lunflow.dao.Model.Group;
import com.example.lunflow.dao.Model.Group;

import java.util.List;

public interface GroupDao {
    List<Group> findAll();
    Group findById(String id);
  List<Collaborator> findCollabList(List<String> collabIdList);

    Integer findnbWrkftype(String id);

}
