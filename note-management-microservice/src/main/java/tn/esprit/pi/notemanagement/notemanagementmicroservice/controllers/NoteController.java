package tn.esprit.pi.notemanagement.notemanagementmicroservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Dtos.NoteDTO;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Entities.Note;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Mappers.NoteMapper;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.services.NoteService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
public class NoteController {
    private final NoteService service;

    // Enseignant (Teacher) : peut noter un étudiant
    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping
    public NoteDTO noter(@RequestBody NoteDTO noteDTO) {
        return NoteMapper.toDto(service.noterEtudiant(NoteMapper.toEntity(noteDTO)));
    }

    // Étudiant : peut voir ses notes
    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/etudiant/{id}")
    public List<NoteDTO> notesParEtudiant(@PathVariable String id) {
        return service.getNotesParEtudiant(id)
                .stream()
                .map(NoteMapper::toDto)
                .collect(Collectors.toList());
    }

    // Enseignant et Admin : peut voir les notes par groupe
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    @GetMapping("/groupe/{id}")
    public List<NoteDTO> notesParGroupe(@PathVariable String id) {
        return service.getNotesParGroupe(id)
                .stream()
                .map(NoteMapper::toDto)
                .collect(Collectors.toList());
    }

    // Enseignant et Admin : peut voir les notes par séance
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    @GetMapping("/seance/{id}")
    public List<NoteDTO> notesParSeance(@PathVariable String id) {
        return service.getNotesParSeance(id)
                .stream()
                .map(NoteMapper::toDto)
                .collect(Collectors.toList());
    }

    // Étudiant : peut consulter sa moyenne
    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/moyenne/etudiant/{id}")
    public ResponseEntity<Double> moyenneEtudiant(@PathVariable String id) {
        return ResponseEntity.ok(service.calculerMoyenneEtudiant(id));
    }

    // Enseignant et Admin : peuvent consulter la moyenne d'une séance
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    @GetMapping("/moyenne/seance/{id}")
    public ResponseEntity<Double> moyenneSeance(@PathVariable String id) {
        return ResponseEntity.ok(service.calculerMoyenneSeance(id));
    }

    // Enseignant et Admin : peuvent consulter la moyenne d'un sprint
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    @GetMapping("/moyenne/sprint/{id}")
    public ResponseEntity<Double> moyenneSprint(@PathVariable String id) {
        return ResponseEntity.ok(service.calculerMoyenneSprint(id));
    }

    // Enseignant : peut noter un étudiant de manière individuelle
    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/individuelle")
    public ResponseEntity<NoteDTO> noterIndividuelle(@RequestBody NoteDTO noteDTO) {
        Note note = NoteMapper.toEntity(noteDTO);
        Note savedNote = service.noterEtudiant(note); // Sauvegarder la note via le service
        NoteDTO savedNoteDTO = NoteMapper.toDto(savedNote); // Mapper l'entité en DTO pour la réponse
        return ResponseEntity.status(201).body(savedNoteDTO); // Retourner la note enregistrée
    }

    // Enseignant : peut noter un groupe d'étudiants
    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/groupe")
    public ResponseEntity<List<NoteDTO>> noterGroupe(@RequestBody List<NoteDTO> notesDTO) {
        List<Note> notes = notesDTO.stream()
                .map(NoteMapper::toEntity) // Mapper chaque DTO en entité
                .collect(Collectors.toList());

        List<Note> savedNotes = service.noterGroupe(notes); // Sauvegarder les notes via le service pour chaque étudiant dans le groupe
        List<NoteDTO> savedNotesDTO = savedNotes.stream()
                .map(NoteMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.status(201).body(savedNotesDTO); // Retourner les notes enregistrées
    }
}
