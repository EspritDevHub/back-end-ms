package tn.esprit.pi.notemanagement.notemanagementmicroservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Dtos.NoteDTO;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Mappers.NoteMapper;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.dtos.NoteDTO;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.mappers.NoteMapper;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.services.NoteService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
public class NoteController {
    private final NoteService service;

    @PostMapping
    public NoteDTO noter(@RequestBody NoteDTO noteDTO) {
        return NoteMapper.toDto(service.noterEtudiant(NoteMapper.toEntity(noteDTO)));
    }

    @GetMapping("/etudiant/{id}")
    public List<NoteDTO> notesParEtudiant(@PathVariable String id) {
        return service.getNotesParEtudiant(id)
                .stream()
                .map(NoteMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/groupe/{id}")
    public List<NoteDTO> notesParGroupe(@PathVariable String id) {
        return service.getNotesParGroupe(id)
                .stream()
                .map(NoteMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/seance/{id}")
    public List<NoteDTO> notesParSeance(@PathVariable String id) {
        return service.getNotesParSeance(id)
                .stream()
                .map(NoteMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/moyenne/etudiant/{id}")
    public ResponseEntity<Double> moyenneEtudiant(@PathVariable String id) {
        return ResponseEntity.ok(service.calculerMoyenneEtudiant(id));
    }

    @GetMapping("/moyenne/seance/{id}")
    public ResponseEntity<Double> moyenneSeance(@PathVariable String id) {
        return ResponseEntity.ok(service.calculerMoyenneSeance(id));
    }

    @GetMapping("/moyenne/sprint/{id}")
    public ResponseEntity<Double> moyenneSprint(@PathVariable String id) {
        return ResponseEntity.ok(service.calculerMoyenneSprint(id));
    }
}
