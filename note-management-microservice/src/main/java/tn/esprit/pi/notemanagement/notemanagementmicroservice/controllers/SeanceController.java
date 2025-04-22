package tn.esprit.pi.notemanagement.notemanagementmicroservice.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Entities.Seance;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.services.SeanceService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/seances")
public class SeanceController {

    @Autowired
    private SeanceService seanceService;

    // Créer une nouvelle séance
    @PostMapping
    public ResponseEntity<Seance> createSeance(@RequestBody Seance seance) {
        Seance createdSeance = seanceService.createSeance(seance);
        return new ResponseEntity<>(createdSeance, HttpStatus.CREATED);
    }

    // Obtenir toutes les séances d'un sprint
    @GetMapping("/sprint/{sprintId}")
    public ResponseEntity<List<Seance>> getSeancesBySprintId(@PathVariable String sprintId) {
        List<Seance> seances = seanceService.getSeancesBySprintId(sprintId);
        return new ResponseEntity<>(seances, HttpStatus.OK);
    }

    // Obtenir une séance par son ID
    @GetMapping("/{id}")
    public ResponseEntity<Seance> getSeanceById(@PathVariable String id) {
        Optional<Seance> seance = seanceService.getSeanceById(id);
        return seance.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Mettre à jour une séance
    @PutMapping("/{id}")
    public ResponseEntity<Seance> updateSeance(@PathVariable String id, @RequestBody Seance seance) {
        Seance updatedSeance = seanceService.updateSeance(id, seance);
        return updatedSeance != null ? new ResponseEntity<>(updatedSeance, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Supprimer une séance
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeance(@PathVariable String id) {
        seanceService.deleteSeance(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
