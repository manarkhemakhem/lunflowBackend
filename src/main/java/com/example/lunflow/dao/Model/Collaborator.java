package com.example.lunflow.dao.Model;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "collaborator")
public class Collaborator {
    @Id
    private String id;
    private String fullname;
    private String title;
    private String email;
    private String groupId;
    private String userId;
    private boolean isAdmin;
    private String idPicture;

}
