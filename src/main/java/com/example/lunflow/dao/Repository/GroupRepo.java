package com.example.lunflow.dao.Repository;

import com.example.lunflow.dao.Model.Collaborator;
import com.example.lunflow.dao.Model.Group;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface GroupRepo  extends MongoRepository<Group, String> {
    List<Collaborator> findByCollabIdListIn(List<String> collabIdList);


}
