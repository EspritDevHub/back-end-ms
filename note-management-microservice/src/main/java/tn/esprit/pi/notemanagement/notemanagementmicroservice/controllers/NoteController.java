package tn.esprit.pi.notemanagement.notemanagementmicroservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Dtos.CritereEvaluationDTO;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Dtos.NoteDTO;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Dtos.SeanceDTO;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Dtos.UserResponseDTO;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Entities.Note;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Enum.TypeNote;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Feign.SeanceClient;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Feign.UserClient;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Mappers.NoteMapper;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.services.NoteService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService service;
    @Qualifier("seanceClient")
    @Autowired
      SeanceClient seanceClient;
    @Qualifier("userClient")
    @Autowired
            UserClient userClient; // Injection du UserClient


    // Fonction pour récupérer le rôle d'un utilisateur via UserClient
    private String getUserRole(String userId) {
        try {
            // Appel au UserClient pour récupérer les détails de l'utilisateur
            return userClient.getUserRole(userId); // Exemple: méthode getUserRole à implémenter dans le Feign Client
        } catch (Exception e) {
            return "ADMIN"; // Retour d'un rôle par défaut en cas d'erreur
        }
    }



    // Enseignant (Teacher) : peut noter un étudiant
    @PostMapping
    public NoteDTO noter(@RequestBody NoteDTO noteDTO) {
        Note note = NoteMapper.toEntity(noteDTO);
        SeanceDTO seance = seanceClient.getSeanceById(note.getSeanceId());

        if (seance == null) {
            throw new IllegalArgumentException("Séance introuvable pour l'ID : " + note.getSeanceId());
        }
        Note savedNote = service.noterEtudiant(note, seance);
        return NoteMapper.toDto(savedNote);
    }

    // Étudiant : peut voir ses notes
    //@PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/etudiant/{id}")
    public List<NoteDTO> notesParEtudiant(@PathVariable String id) {
        return service.getNotesParEtudiant(id)
                .stream()
                .map(NoteMapper::toDto)
                .collect(Collectors.toList());
    }

    // Enseignant et Admin : peuvent consulter les notes par groupe
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    @GetMapping("/groupe/{id}")
    public List<NoteDTO> notesParGroupe(@PathVariable String id) {
        return service.getNotesParGroupe(id)
                .stream()
                .map(NoteMapper::toDto)
                .collect(Collectors.toList());
    }

    // Enseignant et Admin : peuvent consulter les notes par séance
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    @GetMapping("/seance/{id}")
    public List<NoteDTO> notesParSeance(@PathVariable String id) {
        return service.getNotesParSeance(id)
                .stream()
                .map(NoteMapper::toDto)
                .collect(Collectors.toList());
    }

    // Enseignant : peut ajouter une note individuelle
    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/individuelle")
    public ResponseEntity<NoteDTO> noterIndividuelle(@RequestBody NoteDTO noteDTO) {
        Note note = NoteMapper.toEntity(noteDTO);
        SeanceDTO seance = seanceClient.getSeanceById(note.getSeanceId());

        if (seance == null) {
            return ResponseEntity.badRequest().build();
        }

        if (seance.getTypeNote() != TypeNote.INDIVIDUELLE) {
            return ResponseEntity.badRequest().build();
        }

        Note savedNote = service.noterEtudiant(note, seance);
        NoteDTO savedNoteDTO = NoteMapper.toDto(savedNote);

        return ResponseEntity.status(201).body(savedNoteDTO);
    }

    // Enseignant : peut ajouter des notes de groupe
    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/groupe")
    public ResponseEntity<?> noterGroupe(@RequestBody List<NoteDTO> notesDTO) {
        if (notesDTO.isEmpty()) {
            return ResponseEntity.badRequest().body("La liste des notes est vide.");
        }

        // Mapper les DTO en entités Note
        List<Note> notes = notesDTO.stream()
                .map(NoteMapper::toEntity)
                .toList();

        // Récupérer le sprintId (supposons qu'il est dans la première note)
        String sprintId = notes.get(0).getSprintId();

        // Appel au service pour récupérer les séances associées au Sprint
        List<SeanceDTO> seances = seanceClient.getSeancesBySprint(sprintId);

        if (seances.isEmpty()) {
            return ResponseEntity.status(404).body("Aucune séance trouvée pour ce sprint.");
        }

        // Appeler la méthode du service pour noter le groupe
        service.noterGroupe(notes, seances);

        return ResponseEntity.status(201).build();
    }

    // Enseignant et Admin : peuvent consulter la moyenne d'un sprint
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    @GetMapping("/moyenne/sprint/{id}")
    public ResponseEntity<Double> moyenneSprint(@PathVariable String id) {
        return ResponseEntity.ok(service.calculerMoyenneSprint(id));
    }
    @GetMapping("/users/{id}")
    private UserResponseDTO getUserById(String userId) {
            return userClient.getUserById(userId);

    }



}
