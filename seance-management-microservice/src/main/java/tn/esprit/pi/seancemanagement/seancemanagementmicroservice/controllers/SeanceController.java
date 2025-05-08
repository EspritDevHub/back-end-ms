package tn.esprit.pi.seancemanagement.seancemanagementmicroservice.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pi.seancemanagement.seancemanagementmicroservice.Dtos.SeanceDTO;
import tn.esprit.pi.seancemanagement.seancemanagementmicroservice.Entities.Seance;
import tn.esprit.pi.seancemanagement.seancemanagementmicroservice.Mappers.SeanceMapper;
import tn.esprit.pi.seancemanagement.seancemanagementmicroservice.services.SeanceService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/seances")
@RequiredArgsConstructor
public class SeanceController {
    private final SeanceService service;

    // Seul l'enseignant peut créer une séance
    @PreAuthorize("hasAuthority('TEACHER')")
    @PostMapping
    public ResponseEntity<SeanceDTO> create(@RequestBody @Valid SeanceDTO s) {
        Seance created = service.createSeance(SeanceMapper.toEntity(s));
        return ResponseEntity.ok(SeanceMapper.toDto(created));
    }

    // Tous les utilisateurs peuvent voir une séance
    @PreAuthorize("hasAuthority('TEACHER') or hasAuthority('ADMIN') or hasAuthority('STUDENT')")
    @GetMapping("/{id}")
    public ResponseEntity<SeanceDTO> getById(@PathVariable String id) {
        Optional<Seance> seanceOpt = service.getSeanceById(id);
        return seanceOpt.map(seance -> ResponseEntity.ok(SeanceMapper.toDto(seance)))
                .orElse(ResponseEntity.notFound().build());
    }

    // Affichage de toutes les séances pour les utilisateurs autorisés
    @PreAuthorize("hasAuthority('TEACHER') or hasAuthority('ADMIN') or hasAuthority('STUDENT')")
    @GetMapping
    public ResponseEntity<List<SeanceDTO>> all() {
        List<SeanceDTO> dtos = service.getAll()
                .stream()
                .map(SeanceMapper::toDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    // Seul l'enseignant peut mettre à jour une séance
    @PreAuthorize("hasAuthority('TEACHER')")
    @PutMapping("/{id}")
    public ResponseEntity<SeanceDTO> update(@PathVariable String id, @RequestBody SeanceDTO s) {
        Seance updated = service.updateSeance(id, SeanceMapper.toEntity(s));
        return ResponseEntity.ok(SeanceMapper.toDto(updated));
    }

    // Seul l'enseignant peut supprimer une séance
    @PreAuthorize("hasAuthority('TEACHER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.deleteSeance(id);
        return ResponseEntity.noContent().build();
    }



    // L'enseignant peut affecter un sprint à une séance
    @PreAuthorize("hasAuthority('TEACHER')")
    @PutMapping("/{seanceId}/affecter-sprint/{sprintId}")
    public ResponseEntity<SeanceDTO> affecterSprint(@PathVariable String seanceId, @PathVariable String sprintId) {
        Seance seance = service.affecterSprint(seanceId, sprintId);
        return ResponseEntity.ok(SeanceMapper.toDto(seance));
    }
}
