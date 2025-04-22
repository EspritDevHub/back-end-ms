package tn.esprit.pi.notemanagement.notemanagementmicroservice.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Entities.Permission;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.services.PermissionService;

import java.util.List;

@RestController
@RequestMapping("/api/permissions")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    // Create or update permission
    @PostMapping
    public ResponseEntity<Permission> createPermission(@RequestBody Permission permission) {
        Permission savedPermission = permissionService.savePermission(permission);
        return ResponseEntity.ok(savedPermission);
    }

    // Get all permissions by documentId
    @GetMapping("/document/{documentId}")
    public ResponseEntity<List<Permission>> getPermissionsByDocumentId(@PathVariable String documentId) {
        List<Permission> permissions = permissionService.getPermissionsByDocumentId(documentId);
        return ResponseEntity.ok(permissions);
    }

    // Get all permissions by userId
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Permission>> getPermissionsByUserId(@PathVariable String userId) {
        List<Permission> permissions = permissionService.getPermissionsByUserId(userId);
        return ResponseEntity.ok(permissions);
    }

    // Get permission by documentId and userId
    @GetMapping("/document/{documentId}/user/{userId}")
    public ResponseEntity<Permission> getPermissionByDocumentAndUser(
            @PathVariable String documentId,
            @PathVariable String userId) {
        Permission permission = permissionService.getPermissionByDocumentAndUser(documentId, userId);
        if (permission != null) {
            return ResponseEntity.ok(permission);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete permission by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePermission(@PathVariable String id) {
        permissionService.deletePermission(id);
        return ResponseEntity.noContent().build();
    }
}
