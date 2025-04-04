package com.example.lunflow.Service.Impl;

import com.example.lunflow.Service.GroupService;
import com.example.lunflow.dao.CollaboratorDao;
import com.example.lunflow.dao.GroupDao;
import com.example.lunflow.dao.Model.Collaborator;
import com.example.lunflow.dao.Model.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class GroupServiceImpl  implements GroupService {
    @Autowired
    private MongoTemplate mongoTemplate;

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

    public List<String> getCollaboratorList(String groupId) {
        // Création du critère pour l'ID du groupe
        Criteria groupCriteria = Criteria.where("_id").is(groupId);

        // Requête pour obtenir le groupe avec l'attribut collabIdList
        Query query = new Query(groupCriteria);

        // Récupérer le groupe à partir de MongoDB
        Group group = mongoTemplate.findOne(query, Group.class);

        if (group != null) {
            // Récupérer la liste des ID des collaborateurs
            return group.getCollabIdList();
        }

        return null;
    }
    @Override
    // Récupérer les détails des collaborateurs en fonction de leurs IDs
    public List<Collaborator> getCollaboratorDetails(List<String> collabIds) {
        // Vérifier si la liste des IDs est non vide
        if (collabIds != null && !collabIds.isEmpty()) {
            // Requête pour récupérer les collaborateurs dont les IDs sont dans la liste
            Query collaboratorQuery = new Query(Criteria.where("_id").in(collabIds));
            return mongoTemplate.find(collaboratorQuery, Collaborator.class);
        }
        return null;
    }

    @Override
    public Integer getnbWrkftype(String id) {
        return  groupDao.findnbWrkftype(id);
    }
}

