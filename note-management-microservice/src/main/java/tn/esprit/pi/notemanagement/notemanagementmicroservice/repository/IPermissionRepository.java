package tn.esprit.pi.notemanagement.notemanagementmicroservice.repository;



import org.springframework.data.mongodb.repository.MongoRepository;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Entities.Permission;

import java.util.List;

public interface IPermissionRepository extends MongoRepository<Permission, String> {
    // Custom query to get permissions by documentId
    List<Permission> findByDocumentId(String documentId);

    // Custom query to get permissions by userId
    List<Permission> findByUserId(String userId);

    // Custom query to get permission by documentId and userId
    Permission findByDocumentIdAndUserId(String documentId, String userId);
}
