package com.example.lunflow.dao.Model;

import lombok.Data;
import org.springframework.data.annotation.Id;
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
    private List<String> collabIdList;
    private List<String> adminIdList;
    private String createdBy;
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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIdLogo() {
        return idLogo;
    }

    public void setIdLogo(String idLogo) {
        this.idLogo = idLogo;
    }

    public List<String> getCollabIdList() {
        return collabIdList;
    }

    public void setCollabIdList(List<String> collabIdList) {
        this.collabIdList = collabIdList;
    }

    public List<String> getAdminIdList() {
        return adminIdList;
    }

    public void setAdminIdList(List<String> adminIdList) {
        this.adminIdList = adminIdList;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public int getNbCollabs() {
        return nbCollabs;
    }

    public void setNbCollabs(int nbCollabs) {
        this.nbCollabs = nbCollabs;
    }

    public int getNbActifCollabs() {
        return nbActifCollabs;
    }

    public void setNbActifCollabs(int nbActifCollabs) {
        this.nbActifCollabs = nbActifCollabs;
    }

    public int getNbCollabsMax() {
        return nbCollabsMax;
    }

    public void setNbCollabsMax(int nbCollabsMax) {
        this.nbCollabsMax = nbCollabsMax;
    }

    public String getGroupeType() {
        return groupeType;
    }

    public void setGroupeType(String groupeType) {
        this.groupeType = groupeType;
    }

    public String getLangue() {
        return langue;
    }

    public void setLangue(String langue) {
        this.langue = langue;
    }

    public boolean isDisableGroup() {
        return disableGroup;
    }

    public void setDisableGroup(boolean disableGroup) {
        this.disableGroup = disableGroup;
    }

    public boolean isShowLabel() {
        return showLabel;
    }

    public void setShowLabel(boolean showLabel) {
        this.showLabel = showLabel;
    }

    public boolean isShowLogo() {
        return showLogo;
    }

    public void setShowLogo(boolean showLogo) {
        this.showLogo = showLogo;
    }

    public int getNbSpace() {
        return nbSpace;
    }

    public void setNbSpace(int nbSpace) {
        this.nbSpace = nbSpace;
    }

    public int getNbWrkftype() {
        return nbWrkftype;
    }

    public void setNbWrkftype(int nbWrkftype) {
        this.nbWrkftype = nbWrkftype;
    }

    public int getNbOfWorkflowTypePublic() {
        return nbOfWorkflowTypePublic;
    }

    public void setNbOfWorkflowTypePublic(int nbOfWorkflowTypePublic) {
        this.nbOfWorkflowTypePublic = nbOfWorkflowTypePublic;
    }

    public String getDateTimeModification() {
        return dateTimeModification;
    }

    public void setDateTimeModification(String dateTimeModification) {
        this.dateTimeModification = dateTimeModification;
    }

    public List<String> getFolderList() {
        return folderList;
    }

    public void setFolderList(List<String> folderList) {
        this.folderList = folderList;
    }

    public List<String> getFolders() {
        return folders;
    }

    public void setFolders(List<String> folders) {
        this.folders = folders;
    }

    public int getNbTable() {
        return nbTable;
    }

    public void setNbTable(int nbTable) {
        this.nbTable = nbTable;
    }
}