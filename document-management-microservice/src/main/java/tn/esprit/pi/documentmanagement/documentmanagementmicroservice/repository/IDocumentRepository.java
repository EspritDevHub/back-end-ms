package tn.esprit.pi.documentmanagement.documentmanagementmicroservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import tn.esprit.pi.documentmanagement.documentmanagementmicroservice.Entities.Document;

import java.util.List;


import org.springframework.data.mongodb.repository.MongoRepository;
import tn.esprit.pi.documentmanagement.documentmanagementmicroservice.Entities.Document;

import java.util.List;
import java.util.Optional;

public interface IDocumentRepository extends MongoRepository<Document, String> {

    // Trouver un document par assignmentId + etudiantId (pour éviter les doublons)
    Optional<Document> findByAssignmentIdAndEtudiantId(String assignmentId, String etudiantId);

    // Lister tous les documents d'une séance pour un étudiant donné
    List<Document> findBySeanceIdAndEtudiantId(String seanceId, String etudiantId);

    // Lister tous les documents d'un étudiant (historique)
    List<Document> findByEtudiantId(String etudiantId);

    // Trouver un document spécifique par ID et vérifier l'étudiant (pour la suppression)
    Optional<Document> findByIdAndEtudiantId(String id, String etudiantId);

    // Pour les enseignants : lister tous les documents d'une séance
    List<Document> findBySeanceId(String seanceId);

    // Pour les enseignants : lister tous les documents d'un assignment
    List<Document> findByAssignmentId(String assignmentId);
}
