package tn.esprit.pi.documentmanagement.documentmanagementmicroservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.pi.documentmanagement.documentmanagementmicroservice.Entities.Document;
import tn.esprit.pi.documentmanagement.documentmanagementmicroservice.repository.IDocumentRepository;

import java.util.Date;
import java.util.List;

@Service
public class DocumentService {

    @Autowired
    private IDocumentRepository documentRepository;

    // Créer un nouveau document
    public Document createDocument(Document document, String userId) {
        // Définir la date de création et l'utilisateur qui a créé le document
        document.setCreatedAt(new Date());  // Date actuelle
        document.setCreatedBy(userId);      // ID de l'utilisateur (enseignant ou étudiant)

        return documentRepository.save(document);
    }

    // Obtenir les documents par séance
    public List<Document> getDocumentsBySeanceId(String seanceId) {
        return documentRepository.findBySeanceId(seanceId);
    }

    // Obtenir les documents par étudiant
    public List<Document> getDocumentsByEtudiantId(String etudiantId) {
        return documentRepository.findByEtudiantId(etudiantId);
    }

    // Mettre à jour le statut d'un document
    public Document updateStatut(String documentId, String statut) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found"));
        document.setStatut(statut);
        return documentRepository.save(document);
    }
}
