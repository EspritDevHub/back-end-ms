package tn.esprit.pi.notemanagement.notemanagementmicroservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Entities.Note;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.services.NoteService;

import java.util.List;


@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
public class NoteController {
    private final NoteService service;

    @PostMapping
    public Note noter(@RequestBody Note note) {
        return service.noterEtudiant(note);
    }

    @GetMapping("/etudiant/{id}")
    public List<Note> notesParEtudiant(@PathVariable String id) {
        return service.getNotesParEtudiant(id);
    }

    @GetMapping("/groupe/{id}")
    public List<Note> notesParGroupe(@PathVariable String id) {
        return service.getNotesParGroupe(id);
    }

    @GetMapping("/seance/{id}")
    public List<Note> notesParSeance(@PathVariable String id) {
        return service.getNotesParSeance(id);
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
