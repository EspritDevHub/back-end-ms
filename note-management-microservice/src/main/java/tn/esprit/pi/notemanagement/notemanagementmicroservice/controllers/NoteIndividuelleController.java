package tn.esprit.pi.notemanagement.notemanagementmicroservice.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Entities.NoteIndividuelle;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.services.NoteIndividuelleService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/notes-individuelles")
public class NoteIndividuelleController {

    private final NoteIndividuelleService noteIndividuelleService;

    @Autowired
    public NoteIndividuelleController(NoteIndividuelleService noteIndividuelleService) {
        this.noteIndividuelleService = noteIndividuelleService;
    }

    // Ajouter ou mettre à jour une note individuelle
    @PostMapping
    public ResponseEntity<NoteIndividuelle> addOrUpdateNoteIndividuelle(@RequestBody NoteIndividuelle noteIndividuelle) {
        NoteIndividuelle savedNote = noteIndividuelleService.saveNoteIndividuelle(noteIndividuelle);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedNote);
    }

    // Obtenir une note individuelle par utilisateur et séance
    @GetMapping("/{userId}/{seanceId}")
    public ResponseEntity<NoteIndividuelle> getNoteIndividuelle(
            @PathVariable String userId,
            @PathVariable String seanceId) {
        Optional<NoteIndividuelle> noteIndividuelle = noteIndividuelleService.getNoteIndividuelle(userId, seanceId);
        return noteIndividuelle.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Obtenir toutes les notes pour une séance donnée
    @GetMapping("/seance/{seanceId}")
    public ResponseEntity<List<NoteIndividuelle>> getNotesBySeance(@PathVariable String seanceId) {
        List<NoteIndividuelle> notes = noteIndividuelleService.getNotesBySeance(seanceId);
        return ResponseEntity.ok(notes);
    }
}
