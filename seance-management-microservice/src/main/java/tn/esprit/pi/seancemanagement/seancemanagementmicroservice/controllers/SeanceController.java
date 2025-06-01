package tn.esprit.pi.seancemanagement.seancemanagementmicroservice.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pi.seancemanagement.seancemanagementmicroservice.Dtos.SeanceDTO;
import tn.esprit.pi.seancemanagement.seancemanagementmicroservice.Entities.Seance;
import tn.esprit.pi.seancemanagement.seancemanagementmicroservice.Mappers.SeanceMapper;
import tn.esprit.pi.seancemanagement.seancemanagementmicroservice.services.SeanceService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/seances")
@RequiredArgsConstructor
@CrossOrigin("*")
public class SeanceController {
    private final SeanceService service;

    // Seul l'enseignant peut créer une séance
    @PostMapping
    public ResponseEntity<SeanceDTO> create(@RequestBody @Valid SeanceDTO s) {

        Seance created = service.createSeance(SeanceMapper.toEntity(s));
        return ResponseEntity.ok(SeanceMapper.toDto(created));
    }

    // Tous les utilisateurs peuvent voir une séance
    @GetMapping("/{id}")
    public ResponseEntity<SeanceDTO> getById(@PathVariable String id) {
        Optional<Seance> seanceOpt = service.getSeanceById(id);
        return seanceOpt.map(seance -> ResponseEntity.ok(SeanceMapper.toDto(seance)))
                .orElse(ResponseEntity.notFound().build());
    }

    // Affichage de toutes les séances pour les utilisateurs autorisés
    @GetMapping
    public ResponseEntity<List<SeanceDTO>> all() {
        List<SeanceDTO> dtos = service.getAll()
                .stream()
                .map(SeanceMapper::toDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    // Seul l'enseignant peut mettre à jour une séance
    @PutMapping("/{id}")
    public ResponseEntity<SeanceDTO> update(@PathVariable String id, @RequestBody SeanceDTO s) {
        Seance updated = service.updateSeance(id, SeanceMapper.toEntity(s));
        System.err.println("hello"+s);
        System.err.println("helloupdate"+updated);
        return ResponseEntity.ok(SeanceMapper.toDto(updated));
    }

    // Seul l'enseignant peut supprimer une séance
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.deleteSeance(id);
        return ResponseEntity.noContent().build();
    }



    // L'enseignant peut affecter un sprint à une séance
    @PutMapping("/{seanceId}/affecter-sprint/{sprintId}")
    public ResponseEntity<SeanceDTO> affecterSprint(@PathVariable String seanceId, @PathVariable String sprintId) {
        Seance seance = service.affecterSprint(seanceId, sprintId);
        return ResponseEntity.ok(SeanceMapper.toDto(seance));
    }

    @GetMapping("/week")
    public List<Seance> getSeancesForWeek(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end
    ) {
        return service.getSeanceEmploi(start, end);
    }
}
