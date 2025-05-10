package tn.esprit.pi.documentmanagement.documentmanagementmicroservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.pi.documentmanagement.documentmanagementmicroservice.Dtos.DocumentDto;
import tn.esprit.pi.documentmanagement.documentmanagementmicroservice.Entities.Document;
import tn.esprit.pi.documentmanagement.documentmanagementmicroservice.repository.IDocumentRepository;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocumentService {

    @Autowired
    private IDocumentRepository documentRepository;

    @Autowired
    private AssignmentService assignmentService;

    // Soumettre ou mettre à jour un document
    public DocumentDto submitDocument(DocumentDto dto, String etudiantId) {
        // Validate assignment exists and deadline hasn't passed

        Document document = new Document();
        document.setAssignmentId(dto.getAssignmentId());
        document.setEtudiantId(etudiantId);
        document.setType(dto.getType());
        document.setContenu(dto.getContenu());
        document.setNomFichier(dto.getNomFichier());
        document.setCommentaire(dto.getCommentaire());
        document.setStatut("SOUMIS");

        // Set current date for submission
        Date now = new Date();
        document.setDateSoumission(now);

        if(document.getId() == null) {
            document.setCreatedAt(now);
        }
        document.setUpdatedAt(now);

        Document saved = documentRepository.save(document);
        return mapToDetailsDto(saved);
    }
    // Obtenir les documents par séance pour un étudiant
    public List<DocumentDto> getDocumentsBySeance(String seanceId, String etudiantId) {
        return documentRepository.findBySeanceIdAndEtudiantId(seanceId, etudiantId)
                .stream()
                .map(this::mapToDetailsDto)
                .collect(Collectors.toList());
    }

    public List<Document> getAllDocuments( ) {
        return documentRepository.findAll();

    }

    // Supprimer un document (si avant date limite)
    public void deleteDocument(String documentId, String etudiantId) {
        Document document = documentRepository.findByIdAndEtudiantId(documentId, etudiantId)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        assignmentService.validateAssignmentSubmission(document.getAssignmentId());

        documentRepository.delete(document);
    }

    private DocumentDto mapToDetailsDto(Document document) {
        DocumentDto dto = new DocumentDto();
        dto.setId(document.getId());
        dto.setSeanceId(document.getSeanceId());
        dto.setType(document.getType());
        dto.setContenu(document.getContenu());
        dto.setNomFichier(document.getNomFichier());
        dto.setCommentaire(document.getCommentaire());
        dto.setStatut(document.getStatut());
        dto.setDateSoumission(document.getDateSoumission());
        dto.setDateLimite(document.getDateLimite());
        return dto;
    }
}