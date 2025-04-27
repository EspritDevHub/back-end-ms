package tn.esprit.pi.documentmanagement.documentmanagementmicroservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import tn.esprit.pi.documentmanagement.documentmanagementmicroservice.Entities.Document;

import java.util.List;

public interface IDocumentRepository extends MongoRepository<Document, String> {

    List<Document> findBySeanceId(String seanceId);

    List<Document> findByEtudiantId(String etudiantId);

    List<Document> findByEnseignantId(String enseignantId);

    List<Document> findBySeanceIdAndStatut(String seanceId, String statut);
}
