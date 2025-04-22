package tn.esprit.pi.notemanagement.notemanagementmicroservice.Entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Enums.PermissionType;

@Document(collection = "permissions")
public class Permission {

    @Id
    private String id;
    private String documentId;
    private String userId;
    private PermissionType permissionType; // View, Edit, Delete

    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public PermissionType getPermissionType() {
        return permissionType;
    }

    public void setPermissionType(PermissionType permissionType) {
        this.permissionType = permissionType;
    }
}
