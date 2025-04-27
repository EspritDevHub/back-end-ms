package tn.esprit.pi.documentmanagement.documentmanagementmicroservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pi.documentmanagement.documentmanagementmicroservice.Dtos.DocumentDto;
import tn.esprit.pi.documentmanagement.documentmanagementmicroservice.Entities.Document;
import tn.esprit.pi.documentmanagement.documentmanagementmicroservice.services.DocumentService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    // Créer un nouveau document
    @PreAuthorize("hasAnyRole('STUDENT')")
    @PostMapping
    public DocumentDto createDocument(@RequestBody DocumentDto documentDTO, @RequestParam String userId) {
        // Créer un nouvel objet Document
        Document document = new Document();
        document.setSeanceId(documentDTO.getSeanceId());
        document.setEtudiantId(documentDTO.getEtudiantId());
        document.setEnseignantId(documentDTO.getEnseignantId());
        document.setType(documentDTO.getType());
        document.setFichier(documentDTO.getFichier());
        document.setCommentaire(documentDTO.getCommentaire());
        document.setStatut(documentDTO.getStatut());

        // Sauvegarder le document et renvoyer le DTO
        Document savedDocument = documentService.createDocument(document, userId);
        return new DocumentDto(
                savedDocument.getSeanceId(),
                savedDocument.getEtudiantId(),
                savedDocument.getEnseignantId(),
                savedDocument.getType(),
                savedDocument.getFichier(),
                savedDocument.getCommentaire(),
                savedDocument.getStatut()
        );
    }

    // Obtenir les documents par séance
    @PreAuthorize("hasAnyRole('STUDENT') Or hasAnyRole('TEACHER') ")
    @GetMapping("/seance/{seanceId}")
    public List<DocumentDto> getDocumentsBySeance(@PathVariable String seanceId) {
        List<Document> documents = documentService.getDocumentsBySeanceId(seanceId);
        return documents.stream().map(document -> new DocumentDto(
                document.getSeanceId(),
                document.getEtudiantId(),
                document.getEnseignantId(),
                document.getType(),
                document.getFichier(),
                document.getCommentaire(),
                document.getStatut()
        )).collect(Collectors.toList());
    }

    // Obtenir les documents par étudiant
    @PreAuthorize("hasAnyRole('STUDENT') Or hasAnyRole('TEACHER') ")
    @GetMapping("/etudiant/{etudiantId}")
    public List<DocumentDto> getDocumentsByEtudiant(@PathVariable String etudiantId) {
        List<Document> documents = documentService.getDocumentsByEtudiantId(etudiantId);
        return documents.stream().map(document -> new DocumentDto(
                document.getSeanceId(),
                document.getEtudiantId(),
                document.getEnseignantId(),
                document.getType(),
                document.getFichier(),
                document.getCommentaire(),
                document.getStatut()
        )).collect(Collectors.toList());
    }

    // Mettre à jour le statut d'un document
    @PreAuthorize("hasAnyRole('STUDENT') Or hasAnyRole('TEACHER') ")
    @PutMapping("/{documentId}/statut")
    public DocumentDto updateStatut(@PathVariable String documentId, @RequestParam String statut) {
        Document document = documentService.updateStatut(documentId, statut);
        return new DocumentDto(
                document.getSeanceId(),
                document.getEtudiantId(),
                document.getEnseignantId(),
                document.getType(),
                document.getFichier(),
                document.getCommentaire(),
                document.getStatut()
        );
    }
}
