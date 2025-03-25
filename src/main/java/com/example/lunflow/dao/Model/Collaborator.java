package com.example.lunflow.dao.Model;


import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "collaborator")
public class Collaborator {
    @Id
    private ObjectId id;
    private String fullname;
    private String title;
    private String email;
    private String groupId;
    private String userId;
    private boolean isAdmin;
    private String idPicture;private Boolean deleted;
    private Boolean OnLine;
    public ObjectId getId() {
        return id;
    }
    public Boolean getDeleted() {
        return deleted;
    }public Boolean getOnLine() {
        return OnLine;
    }
}
