package tn.esprit.pi.notemanagement.notemanagementmicroservice.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Entities.CritereEvaluation;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.services.CritereEvaluationService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/criteres-evaluation")
public class CritereEvaluationController {

    @Autowired
    private CritereEvaluationService critereEvaluationService;

    // Récupérer tous les critères d'évaluation
    @GetMapping
    public List<CritereEvaluation> getAllCritereEvaluations() {
        return critereEvaluationService.getAllCritereEvaluations();
    }

    // Récupérer les critères d'un sprint par ID
    @GetMapping("/sprint/{sprintId}")
    public List<CritereEvaluation> getCritereEvaluationsBySprintId(@PathVariable String sprintId) {
        return critereEvaluationService.getCritereEvaluationsBySprintId(sprintId);
    }

    // Créer un critère d'évaluation
    @PostMapping
    public ResponseEntity<CritereEvaluation> createCritereEvaluation(@RequestBody CritereEvaluation critereEvaluation) {
        CritereEvaluation createdCritereEvaluation = critereEvaluationService.createCritereEvaluation(critereEvaluation);
        return ResponseEntity.ok(createdCritereEvaluation);
    }

    // Mettre à jour un critère d'évaluation
    @PutMapping("/{id}")
    public ResponseEntity<CritereEvaluation> updateCritereEvaluation(@PathVariable String id, @RequestBody CritereEvaluation critereEvaluation) {
        critereEvaluation.setId(id);
        CritereEvaluation updatedCritereEvaluation = critereEvaluationService.updateCritereEvaluation(critereEvaluation);
        return ResponseEntity.ok(updatedCritereEvaluation);
    }

    // Supprimer un critère d'évaluation
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCritereEvaluation(@PathVariable String id) {
        critereEvaluationService.deleteCritereEvaluation(id);
        return ResponseEntity.noContent().build();
    }
}

