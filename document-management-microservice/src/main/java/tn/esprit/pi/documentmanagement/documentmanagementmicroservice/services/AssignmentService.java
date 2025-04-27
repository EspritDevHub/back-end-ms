package tn.esprit.pi.documentmanagement.documentmanagementmicroservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.pi.documentmanagement.documentmanagementmicroservice.Dtos.AssignmentDto;
import tn.esprit.pi.documentmanagement.documentmanagementmicroservice.Entities.Assignment;
import tn.esprit.pi.documentmanagement.documentmanagementmicroservice.repository.IAssignmentRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
@Service
public class AssignmentService {

    @Autowired
    private IAssignmentRepository assignmentRepository;

    // Créer un nouvel assignment
    public Assignment createAssignment(AssignmentDto assignmentDTO, String userId) {
        // Créer l'entité Assignment à partir du DTO
        Assignment assignment = new Assignment();
        assignment.setSeanceId(assignmentDTO.getSeanceId());
        assignment.setEnseignantId(assignmentDTO.getEnseignantId());
        assignment.setType(assignmentDTO.getType());
        assignment.setDescription(assignmentDTO.getDescription());
        assignment.setDateLimite(assignmentDTO.getDateLimite());
        assignment.setStatut("à faire"); // Statut initial
        assignment.setCreatedAt(new Date()); // Date de création
        assignment.setCreatedBy(userId);    // Utilisateur qui crée le travail (enseignant ou étudiant)

        // Sauvegarder et retourner l'assignement
        return assignmentRepository.save(assignment);
    }

    // Méthode pour le dépôt de travail par un étudiant
    public Assignment submitAssignment(String assignmentId, String userId) {
        // On récupère l'assignement existant
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        // On définit les informations liées au dépôt
        assignment.setStatut("terminé"); // Par exemple, le statut peut être mis à "terminé" lors du dépôt
        assignment.setCreatedAt(new Date()); // Mise à jour de la date de dépôt

        // On enregistre le travail soumis
        return assignmentRepository.save(assignment);
    }
}
