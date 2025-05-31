package tn.esprit.pi.documentmanagement.documentmanagementmicroservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.pi.documentmanagement.documentmanagementmicroservice.Dtos.AssignmentDto;
import tn.esprit.pi.documentmanagement.documentmanagementmicroservice.Entities.Assignment;
import tn.esprit.pi.documentmanagement.documentmanagementmicroservice.repository.IAssignmentRepository;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public List<Assignment> getAssignmentsDueInNextDays(int daysAhead) {
        LocalDate now = LocalDate.now();
        LocalDate targetDate = now.plusDays(daysAhead);

        return assignmentRepository.findAll().stream()
                .filter(a -> {
                    Date dateLimite = a.getDateLimite();
                    if (dateLimite == null) return false;

                    LocalDate due = dateLimite.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    return !due.isBefore(now) && !due.isAfter(targetDate);
                })
                .sorted(Comparator.comparing(a -> a.getDateLimite()))
                .collect(Collectors.toList());
    }


    public Assignment createAssignment(AssignmentDto dto) {
        Assignment assignment = new Assignment();
        assignment.setTitre(dto.getTitre());
        assignment.setDescription(dto.getDescription());
        assignment.setSeanceId(dto.getSeanceId());
        assignment.setEnseignantId("ENS0001");
        assignment.setType(dto.getType());
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
                    existing.setType(dto.getType());
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

    // DELETE
    public void deleteAll() {
        assignmentRepository.deleteAll();
    }
}