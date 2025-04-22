package tn.esprit.pi.notemanagement.notemanagementmicroservice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Entities.Document;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Entities.Seance;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Enums.DocumentStatus;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.repository.IDocumentRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DocumentService {

    @Autowired
    private final IDocumentRepository documentRepository;

    // Ajouter un nouveau document
    public Document ajouterDocument(Document document, Seance seance) {
        document.setSeanceId(seance.getId());  // Lier le document à la séance par son ID
        document.setStatus(DocumentStatus.IN_PROGRESS);  // Statut par défaut
        return documentRepository.save(document);
    }

    // Récupérer tous les documents associés à une séance
    public List<Document> recupererDocumentsParSeance(String seanceId) {
        return documentRepository.findBySeanceId(seanceId);
    }
}
