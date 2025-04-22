package tn.esprit.pi.notemanagement.notemanagementmicroservice.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Entities.Version;

import java.util.List;

public interface IVersionRepository extends MongoRepository<Version, String> {
    List<Version> findByDocumentId(String documentId);
}
