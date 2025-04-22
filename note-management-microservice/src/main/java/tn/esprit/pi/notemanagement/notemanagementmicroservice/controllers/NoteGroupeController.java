package tn.esprit.pi.notemanagement.notemanagementmicroservice.controllers;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Entities.NoteGroupe;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.services.NoteGroupeService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/notes-groupes")
public class NoteGroupeController {

    @Autowired
    private NoteGroupeService noteGroupeService;

    // Créer une NoteGroupe
    @PostMapping
    public ResponseEntity<NoteGroupe> createNoteGroupe(@RequestBody NoteGroupe noteGroupe) {
        NoteGroupe createdNoteGroupe = noteGroupeService.createNoteGroupe(noteGroupe);
        return new ResponseEntity<>(createdNoteGroupe, HttpStatus.CREATED);
    }

    // Récupérer toutes les NoteGroupes par séance
    @GetMapping("/seance/{seanceId}")
    public ResponseEntity<List<NoteGroupe>> getNoteGroupesBySeance(@PathVariable String seanceId) {
        List<NoteGroupe> noteGroupes = noteGroupeService.getNoteGroupesBySeanceId(seanceId);
        return new ResponseEntity<>(noteGroupes, HttpStatus.OK);
    }

    // Récupérer une NoteGroupe par ID
    @GetMapping("/{id}")
    public ResponseEntity<NoteGroupe> getNoteGroupeById(@PathVariable String id) {
        Optional<NoteGroupe> noteGroupe = noteGroupeService.getNoteGroupeById(id);
        if (noteGroupe.isPresent()) {
            return new ResponseEntity<>(noteGroupe.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Mettre à jour une NoteGroupe
    @PutMapping("/{id}")
    public ResponseEntity<NoteGroupe> updateNoteGroupe(@PathVariable String id, @RequestBody NoteGroupe noteGroupe) {
        NoteGroupe updatedNoteGroupe = noteGroupeService.updateNoteGroupe(id, noteGroupe);
        if (updatedNoteGroupe != null) {
            return new ResponseEntity<>(updatedNoteGroupe, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Supprimer une NoteGroupe
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNoteGroupe(@PathVariable String id) {
        noteGroupeService.deleteNoteGroupe(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

