package tn.esprit.pi.documentmanagement.documentmanagementmicroservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pi.documentmanagement.documentmanagementmicroservice.Dtos.AssignmentDto;
import tn.esprit.pi.documentmanagement.documentmanagementmicroservice.Entities.Assignment;
import tn.esprit.pi.documentmanagement.documentmanagementmicroservice.services.AssignmentService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/assignments")
public class AssignmentController {

    @Autowired
    private AssignmentService assignmentService;

    @PostMapping
    public Assignment createAssignment(@RequestBody AssignmentDto dto)
    {
        return assignmentService.createAssignment(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Assignment> getAssignment(@PathVariable String id) {
        Optional<Assignment> assignment = assignmentService.getAssignmentById(id);
        return assignment.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<Assignment> getAllAssignments() {
        return assignmentService.getAllAssignments();
    }

    @GetMapping("/seance/{seanceId}")
    public List<Assignment> getAssignmentsBySeance(@PathVariable String seanceId) {
        return assignmentService.getAssignmentsBySeance(seanceId);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ENSEIGNANT')")
    public Assignment updateAssignment(@PathVariable String id, @RequestBody AssignmentDto dto) {
        return assignmentService.updateAssignment(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ENSEIGNANT')")
    public void deleteAssignment(@PathVariable String id) {
        assignmentService.deleteAssignment(id);
    }

    @DeleteMapping("")
    @PreAuthorize("hasRole('ENSEIGNANT')")
    public void deleteall() {
        assignmentService.deleteAll();
    }
}