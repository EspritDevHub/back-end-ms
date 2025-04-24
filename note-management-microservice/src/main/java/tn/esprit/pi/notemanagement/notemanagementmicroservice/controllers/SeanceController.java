package tn.esprit.pi.notemanagement.notemanagementmicroservice.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Entities.CritereEvaluation;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Entities.Seance;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.repository.ICritereEvaluationRepository;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.repository.ISeanceRepository;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.services.SeanceService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/seances")
@RequiredArgsConstructor
public class SeanceController {
    private final SeanceService service;
    @Autowired
    private SeanceService seanceService;

    @PostMapping
    public Seance create(@RequestBody Seance s) {
        return service.createSeance(s);
    }

    @GetMapping
    public List<Seance> all() {
        return service.getAll();
    }

    @PutMapping("/{id}")
    public Seance update(@PathVariable String id, @RequestBody Seance s) {
        return service.updateSeance(id, s);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.deleteSeance(id);
    }

    @PutMapping("/{id}/affecter-criteres-par-nom")
    public Seance affecterCriteresParNom(@PathVariable String id, @RequestBody List<String> critereNoms) {
        return seanceService.affecterCriteresParNom(id, critereNoms);
    }
    @PutMapping("/{seanceId}/affecter-sprint/{sprintId}")
    public Seance affecterSprint(@PathVariable String seanceId, @PathVariable String sprintId) {
        return seanceService.affecterSprint(seanceId, sprintId);
    }

}