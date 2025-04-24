package tn.esprit.pi.notemanagement.notemanagementmicroservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Dtos.NoteDTO;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Entities.Note;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Entities.Seance;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Enum.TypeNote;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Mappers.NoteMapper;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.repository.ISeanceRepository;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.services.NoteService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
public class NoteController {
    private final NoteService service;
    @Autowired
    private ISeanceRepository seanceRepository;
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

    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/individuelle")
    public ResponseEntity<NoteDTO> noterIndividuelle(@RequestBody NoteDTO noteDTO) {
        Note note = NoteMapper.toEntity(noteDTO);

        // Vérifier si la séance est bien de type "INDIVIDUELLE" via seanceId
        Seance seance = seanceRepository.findById(note.getSeanceId())
                .orElseThrow(() -> new IllegalArgumentException("Séance non trouvée avec l'ID: " + note.getSeanceId()));

        if (seance.getTypeNote() != TypeNote.INDIVIDUELLE) {
            return ResponseEntity.badRequest().body(null); // Retourner une erreur si ce n'est pas le bon type
        }

        Note savedNote = service.noterEtudiant(note);
        NoteDTO savedNoteDTO = NoteMapper.toDto(savedNote);
        return ResponseEntity.status(201).body(savedNoteDTO);
    }

    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/groupe")
    public ResponseEntity<List<NoteDTO>> noterGroupe(@RequestBody List<NoteDTO> notesDTO) {
        List<Note> notes = notesDTO.stream()
                .map(NoteMapper::toEntity)
                .collect(Collectors.toList());

        // Vérifier que toutes les séances sont bien de type "GROUPE" via seanceId
        for (Note note : notes) {
            Seance seance = seanceRepository.findById(note.getSeanceId())
                    .orElseThrow(() -> new IllegalArgumentException("Séance non trouvée avec l'ID: " + note.getSeanceId()));

            if (seance.getTypeNote() != TypeNote.GROUPE) {
                return ResponseEntity.badRequest().body(null); // Retourner une erreur si ce n'est pas le bon type
            }
        }

        List<Note> savedNotes = service.noterGroupe(notes);
        List<NoteDTO> savedNotesDTO = savedNotes.stream()
                .map(NoteMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.status(201).body(savedNotesDTO);
    }



}
