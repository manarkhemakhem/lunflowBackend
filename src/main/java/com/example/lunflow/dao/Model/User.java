package com.example.lunflow.dao.Model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@Document(collection = "user")
public class User {
    @Id
    private String id;
    private String firstname;
    private String lastname;
    private String username;
    private String email;
    private LocalDateTime creationDate;
    private LocalDateTime dateTimeModification;
    private boolean enterPassword;
    private boolean administrator;
    private String password;
    private String lastCollabId;
    private String lastCollabFullName;
    private String lastCollabTitle;
    private String lastCollabGroupId;
    private String lastCollabGroupLabel;
    private List<String> tabCollaborator;
    private String langue;
    private boolean confirmed;
    private String verificationId;
    private boolean showHelp;
    private boolean needChangePassword;
    private boolean updatePassword;
    private boolean blocked;
    public LocalDateTime getcreationDate() {
        return creationDate;
    }

}
