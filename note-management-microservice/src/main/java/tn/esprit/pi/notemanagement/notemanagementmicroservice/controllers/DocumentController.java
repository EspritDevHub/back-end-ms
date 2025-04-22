package tn.esprit.pi.notemanagement.notemanagementmicroservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Entities.Document;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Entities.Seance;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.services.DocumentService;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.services.SeanceService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;
    private final SeanceService seanceService;

    // Ajouter un nouveau document
    @PostMapping
    public ResponseEntity<?> ajouterDocument(@RequestBody Document document) {
       Optional<Seance> optionalSeance = seanceService.getSeanceById(document.getSeanceId());

        if (optionalSeance.isPresent()) {
            Seance seance = optionalSeance.get();
            document.setSeanceId(seance.getId());

            Document savedDocument = documentService.ajouterDocument(document, seance);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedDocument);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Séance introuvable pour l'ID spécifié");
        }
    }

    // Récupérer tous les documents d'une séance
    @GetMapping("/seance/{seanceId}")
    public List<Document> recupererDocumentsParSeance(@PathVariable String seanceId) {
        return documentService.recupererDocumentsParSeance(seanceId);
    }
}
