package tn.esprit.pi.notemanagement.notemanagementmicroservice.repository;



import org.springframework.data.mongodb.repository.MongoRepository;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Entities.Document;

import java.util.List;


public interface IDocumentRepository extends MongoRepository<Document, String> {


    // Trouver les documents d'un utilisateur par ID
    List<Document> findByUserId(String userId);

    // Trouver les documents d'une séance par ID
    List<Document> findBySeanceId(String seanceId);
}
