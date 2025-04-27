package tn.esprit.pi.documentmanagement.documentmanagementmicroservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pi.documentmanagement.documentmanagementmicroservice.Dtos.AssignmentDto;
import tn.esprit.pi.documentmanagement.documentmanagementmicroservice.Entities.Assignment;
import tn.esprit.pi.documentmanagement.documentmanagementmicroservice.services.AssignmentService;

import java.util.Date;
import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/api/assignments")
public class AssignmentController {

    @Autowired
    private AssignmentService assignmentService;

    // Créer un nouvel assignment
    @PreAuthorize("hasAnyRole('TEACHER')")
    @PostMapping
    public AssignmentDto createAssignment(@RequestBody AssignmentDto assignmentDTO, @RequestParam String userId) {
        // Créer l'assignement en appelant le service
        Assignment savedAssignment = assignmentService.createAssignment(assignmentDTO, userId);

        // Retourner le DTO de l'assignement créé
        return new AssignmentDto(
                savedAssignment.getId(),
                savedAssignment.getSeanceId(),
                savedAssignment.getEnseignantId(),
                savedAssignment.getType(),
                savedAssignment.getDescription(),
                savedAssignment.getDateLimite(),
                savedAssignment.getStatut()
        );
    }

    // Soumettre un assignment (pour les étudiants)
    @PreAuthorize("hasAnyRole('TEACHER')")
    @PutMapping("/{assignmentId}/submit")
    public AssignmentDto submitAssignment(@PathVariable String assignmentId, @RequestParam String userId) {
        Assignment savedAssignment = assignmentService.submitAssignment(assignmentId, userId);

        // Retourner le DTO de l'assignement mis à jour
        return new AssignmentDto(
                savedAssignment.getId(),
                savedAssignment.getSeanceId(),
                savedAssignment.getEnseignantId(),
                savedAssignment.getType(),
                savedAssignment.getDescription(),
                savedAssignment.getDateLimite(),
                savedAssignment.getStatut()
        );
    }
}
