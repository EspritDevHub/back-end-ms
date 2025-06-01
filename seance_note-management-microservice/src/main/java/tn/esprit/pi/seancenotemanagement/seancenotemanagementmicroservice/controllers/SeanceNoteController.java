package tn.esprit.pi.seancenotemanagement.seancenotemanagementmicroservice.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pi.seancenotemanagement.seancenotemanagementmicroservice.Dtos.SeanceNoteDTO;
import tn.esprit.pi.seancenotemanagement.seancenotemanagementmicroservice.Entities.SeanceNote;
import tn.esprit.pi.seancenotemanagement.seancenotemanagementmicroservice.Mappers.SeanceNoteMapper;
import tn.esprit.pi.seancenotemanagement.seancenotemanagementmicroservice.services.SeanceNoteService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/seances")
@RequiredArgsConstructor
public class SeanceNoteController {
    private final SeanceNoteService service;

    // Seul l'enseignant peut créer une séance
    @PreAuthorize("hasAuthority('TEACHER')")
    @PostMapping
    public ResponseEntity<SeanceNoteDTO> create(@RequestBody @Valid SeanceNoteDTO s) {
        SeanceNote created = service.createSeance(SeanceNoteMapper.toEntity(s));
        return ResponseEntity.ok(SeanceNoteMapper.toDto(created));
    }

    // Tous les utilisateurs peuvent voir une séance
    @PreAuthorize("hasAuthority('TEACHER') or hasAuthority('ADMIN') or hasAuthority('STUDENT')")
    @GetMapping("/{id}")
    public ResponseEntity<SeanceNoteDTO> getById(@PathVariable String id) {
        Optional<SeanceNote> seanceOpt = service.getSeanceById(id);
        return seanceOpt.map(seance -> ResponseEntity.ok(SeanceNoteMapper.toDto(seance)))
                .orElse(ResponseEntity.notFound().build());
    }

    // Affichage de toutes les séances pour les utilisateurs autorisés
    @PreAuthorize("hasAuthority('TEACHER') or hasAuthority('ADMIN') or hasAuthority('STUDENT')")
    @GetMapping
    public ResponseEntity<List<SeanceNoteDTO>> all() {
        List<SeanceNoteDTO> dtos = service.getAll()
                .stream()
                .map(SeanceNoteMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // Seul l'enseignant peut mettre à jour une séance
    @PutMapping("/{id}")
    public ResponseEntity<SeanceNoteDTO> update(@PathVariable String id, @RequestBody SeanceNoteDTO s) {
        SeanceNote updated = service.updateSeance(id, SeanceNoteMapper.toEntity(s));
        return ResponseEntity.ok(SeanceNoteMapper.toDto(updated));
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
    public ResponseEntity<SeanceNoteDTO> affecterSprint(@PathVariable String seanceId, @PathVariable String sprintId) {
        SeanceNote seance = service.affecterSprint(seanceId, sprintId);
        return ResponseEntity.ok(SeanceNoteMapper.toDto(seance));
    }
}
