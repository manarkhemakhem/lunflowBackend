package com.example.lunflow.dao.Model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "group")
public class Group {

    @Id
    private String id;

    private String label;
    private String address;
    private String idLogo;

    private List<String> collabIdList;  // Liste des IDs des collaborateurs

    private List<String> adminIdList;  // List of IDs of admins
    private String createdBy;  // ID of user who created the group
    private int nbCollabs;
    private int nbActifCollabs;
    private int nbCollabsMax;
    private String langue;
    private String groupeType;
    private boolean disableGroup;
    private boolean showLabel;
    private boolean showLogo;
    private int nbSpace;
    private int nbWrkftype;
    private int nbTable;
    private List<String> folders;
    private List<String> folderList;
    private String dateTimeModification;
    private int nbOfWorkflowTypePublic;

    public List<String> getCollabIdList() {
        return  collabIdList;

    }    // Getter pour nbWrkftype
    public Integer getnbWrkftype() {
        return nbWrkftype;
    }
}
