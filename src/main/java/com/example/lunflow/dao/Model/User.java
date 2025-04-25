package com.example.lunflow.dao.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@Document(collection ="user")
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getDateTimeModification() {
        return dateTimeModification;
    }

    public void setDateTimeModification(LocalDateTime dateTimeModification) {
        this.dateTimeModification = dateTimeModification;
    }

    public boolean isEnterPassword() {
        return enterPassword;
    }

    public void setEnterPassword(boolean enterPassword) {
        this.enterPassword = enterPassword;
    }

    public boolean isAdministrator() {
        return administrator;
    }

    public void setAdministrator(boolean administrator) {
        this.administrator = administrator;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLastCollabId() {
        return lastCollabId;
    }

    public void setLastCollabId(String lastCollabId) {
        this.lastCollabId = lastCollabId;
    }

    public String getLastCollabFullName() {
        return lastCollabFullName;
    }

    public void setLastCollabFullName(String lastCollabFullName) {
        this.lastCollabFullName = lastCollabFullName;
    }

    public String getLastCollabTitle() {
        return lastCollabTitle;
    }

    public void setLastCollabTitle(String lastCollabTitle) {
        this.lastCollabTitle = lastCollabTitle;
    }

    public String getLastCollabGroupId() {
        return lastCollabGroupId;
    }

    public void setLastCollabGroupId(String lastCollabGroupId) {
        this.lastCollabGroupId = lastCollabGroupId;
    }

    public String getLastCollabGroupLabel() {
        return lastCollabGroupLabel;
    }

    public void setLastCollabGroupLabel(String lastCollabGroupLabel) {
        this.lastCollabGroupLabel = lastCollabGroupLabel;
    }

    public List<String> getTabCollaborator() {
        return tabCollaborator;
    }

    public void setTabCollaborator(List<String> tabCollaborator) {
        this.tabCollaborator = tabCollaborator;
    }

    public String getLangue() {
        return langue;
    }

    public void setLangue(String langue) {
        this.langue = langue;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public boolean isUpdatePassword() {
        return updatePassword;
    }

    public void setUpdatePassword(boolean updatePassword) {
        this.updatePassword = updatePassword;
    }

    public boolean isNeedChangePassword() {
        return needChangePassword;
    }

    public void setNeedChangePassword(boolean needChangePassword) {
        this.needChangePassword = needChangePassword;
    }

    public boolean isShowHelp() {
        return showHelp;
    }

    public void setShowHelp(boolean showHelp) {
        this.showHelp = showHelp;
    }

    public String getVerificationId() {
        return verificationId;
    }

    public void setVerificationId(String verificationId) {
        this.verificationId = verificationId;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }
}
