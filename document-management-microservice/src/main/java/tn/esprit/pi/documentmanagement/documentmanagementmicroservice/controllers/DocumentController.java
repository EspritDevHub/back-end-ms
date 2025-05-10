package tn.esprit.pi.documentmanagement.documentmanagementmicroservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pi.documentmanagement.documentmanagementmicroservice.Dtos.DocumentDto;
import tn.esprit.pi.documentmanagement.documentmanagementmicroservice.Entities.Document;
import tn.esprit.pi.documentmanagement.documentmanagementmicroservice.services.DocumentService;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/documents")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    // Soumettre ou mettre à jour un document
    @PostMapping
    @PreAuthorize("hasRole('ETUDIANT')")
    public DocumentDto submitDocument(
            @RequestBody DocumentDto dto,
            @RequestHeader("X-User-ID") String etudiantId) {
        return documentService.submitDocument(dto, etudiantId);
    }

    // Obtenir les documents par séance
    @GetMapping("/seance/{seanceId}")
    @PreAuthorize("hasAnyRole('ETUDIANT', 'ENSEIGNANT')")
    public List<DocumentDto> getDocumentsBySeance(
            @PathVariable String seanceId,
            @RequestHeader("X-User-ID") String userId) {
        return documentService.getDocumentsBySeance(seanceId, userId);
    }

    // Obtenir les documents par séance
    @GetMapping("/documents")
    public List<Document> getAllDocuments(){
         return documentService.getAllDocuments();
    }


    // Supprimer un document
    @DeleteMapping("/{documentId}")
    @PreAuthorize("hasRole('ETUDIANT')")
    public ResponseEntity<Void> deleteDocument(
            @PathVariable String documentId,
            @RequestHeader("X-User-ID") String etudiantId) {
        documentService.deleteDocument(documentId, etudiantId);
        return ResponseEntity.noContent().build();
    }
}