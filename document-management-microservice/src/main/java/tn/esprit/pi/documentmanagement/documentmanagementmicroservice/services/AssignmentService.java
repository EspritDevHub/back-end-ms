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
    // Dans AssignmentService.java
    public void validateAssignmentSubmission(String assignmentId) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        if(new Date().after(assignment.getDateLimite())) {
            throw new RuntimeException("La date limite pour cet assignment est dépassée");
        }
    }
    // CREATE
    public Assignment createAssignment(AssignmentDto dto, String enseignantId) {
        Assignment assignment = new Assignment();
        assignment.setTitre(dto.getTitre());
        assignment.setDescription(dto.getDescription());
        assignment.setSeanceId(dto.getSeanceId());
        assignment.setEnseignantId(enseignantId);
        assignment.setType(dto.getTypeRendu());
        assignment.setDateLimite(dto.getDateLimite());
        assignment.setStatut("A_FAIRE");
        assignment.setCreatedAt(new Date());

        return assignmentRepository.save(assignment);
    }

    // READ (single)
    public Optional<Assignment> getAssignmentById(String id) {
        return assignmentRepository.findById(id);
    }

    // READ (all)
    public List<Assignment> getAllAssignments() {
        return assignmentRepository.findAll();
    }

    // READ (by seance)
    public List<Assignment> getAssignmentsBySeance(String seanceId) {
        return assignmentRepository.findBySeanceId(seanceId);
    }

    // UPDATE
    public Assignment updateAssignment(String id, AssignmentDto dto) {
        return assignmentRepository.findById(id)
                .map(existing -> {
                    existing.setTitre(dto.getTitre());
                    existing.setDescription(dto.getDescription());
                    existing.setType(dto.getTypeRendu());
                    existing.setDateLimite(dto.getDateLimite());
                    existing.setCreatedAt(new Date());
                    return assignmentRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Assignment not found"));
    }

    // DELETE
    public void deleteAssignment(String id) {
        assignmentRepository.deleteById(id);
    }
}