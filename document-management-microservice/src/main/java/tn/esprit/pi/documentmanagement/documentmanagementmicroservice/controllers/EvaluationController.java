package tn.esprit.pi.documentmanagement.documentmanagementmicroservice.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pi.documentmanagement.documentmanagementmicroservice.Dtos.EvaluationDto;
import tn.esprit.pi.documentmanagement.documentmanagementmicroservice.Dtos.EvaluationRequest;
import tn.esprit.pi.documentmanagement.documentmanagementmicroservice.services.EvaluationService;

import java.util.List;

@RestController
@RequestMapping("/api/evaluations")
@RequiredArgsConstructor
public class EvaluationController {

    private final EvaluationService evaluationService;

    @PostMapping
    public EvaluationDto evaluer(@RequestBody EvaluationDto dto) {
        return evaluationService.evaluerDocument(dto);
    }
    @PostMapping("/evaluer-text")
    public EvaluationDto evaluerDocumentTexte(@RequestBody EvaluationRequest request) {
        return evaluationService.evaluerDocumentText(request.getEvaluationDto(), request.getDocumentDto());
    }

    @GetMapping("/document/{documentId}")
    public List<EvaluationDto> getByDocument(@PathVariable String documentId) {
        return evaluationService.getEvaluationsByDocument(documentId);
    }

    @GetMapping("/enseignant/{enseignantId}")
    public List<EvaluationDto> getByEnseignant(@PathVariable String enseignantId) {
        return evaluationService.getEvaluationsByEnseignant(enseignantId);
    }


    @PostMapping("/analyser-texte")
    @PreAuthorize( "hasRole('ENSEIGNANT')")
    public ResponseEntity<EvaluationDto> analyserTexte(
            @RequestBody String texteRendu,
            @RequestHeader("X-User-ID") String enseignantId) {
        EvaluationDto suggestion = evaluationService.analyserTexteAutomatiquement(texteRendu);
        return ResponseEntity.ok(suggestion);
    }


    @PostMapping("/analyser-git")
    @PreAuthorize("hasRole('ENSEIGNANT')")
    public ResponseEntity<EvaluationDto> analyserDepuisLienGit(@RequestBody String lienGit) {

        if (lienGit != null && lienGit.startsWith("\"") && lienGit.endsWith("\"")) {
            lienGit = lienGit.substring(1, lienGit.length() - 1);
        }
        EvaluationDto evaluation = evaluationService.analyserDepotGit(lienGit);
        return ResponseEntity.ok(evaluation);
    }

    @PostMapping("/analyse-pdf")
    public ResponseEntity<EvaluationDto> analyserDepuisLienPdf(@RequestBody String pdfUrl) {
        EvaluationDto evaluation = evaluationService.analyserCahierDesCharges("http://localhost:4200/assets/test.pdf");
        return ResponseEntity.ok(evaluation);
    }


}
