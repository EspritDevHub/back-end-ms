package tn.esprit.pi.notemanagement.notemanagementmicroservice.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Entities.Permission;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.repository.IPermissionRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PermissionService {

    @Autowired
    private IPermissionRepository permissionRepository;

    // Create or update permission
    public Permission savePermission(Permission permission) {
        return permissionRepository.save(permission);
    }

    // Get all permissions for a document
    public List<Permission> getPermissionsByDocumentId(String documentId) {
        return permissionRepository.findByDocumentId(documentId);
    }

    // Get permissions for a user
    public List<Permission> getPermissionsByUserId(String userId) {
        return permissionRepository.findByUserId(userId);
    }

    // Get permission by documentId and userId
    public Permission getPermissionByDocumentAndUser(String documentId, String userId) {
        return permissionRepository.findByDocumentIdAndUserId(documentId, userId);
    }

    // Delete a permission by ID
    public void deletePermission(String id) {
        permissionRepository.deleteById(id);
    }
}
