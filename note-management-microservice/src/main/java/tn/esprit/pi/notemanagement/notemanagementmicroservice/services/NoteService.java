package tn.esprit.pi.notemanagement.notemanagementmicroservice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Dtos.CoursDTO;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Dtos.CritereEvaluationDTO;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Dtos.NoteDTO;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Dtos.SeanceDTO;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Entities.CritereEvaluation;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Entities.Note;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Enum.TypeNote;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Feign.SeanceClient;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Mappers.CritereEvaluationMapper;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.repository.CoursRepository;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.repository.INoteRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class NoteService {
    private final INoteRepository noteRepo;
    @Autowired
    private final CoursRepository coursRepository;
    SeanceClient seanceClient; // Injection du FeignClient

    // Récupère les notes d'un étudiant par ID
    public List<Note> getNotesParEtudiant(String etudiantId) {
        return noteRepo.findByEtudiantId(etudiantId);
    }

    // Récupère les séances associées à un sprint via le FeignClient
    public List<SeanceDTO> getSeancesBySprint(String sprintId) {
        return seanceClient.getSeancesBySprint(sprintId);
    }

    // Récupère les notes par groupe
    public List<Note> getNotesParGroupe(String groupeId) {
        return noteRepo.findByGroupeId(groupeId);
    }

    // Récupère les notes par séance
    public List<Note> getNotesParSeance(String seanceId) {
        return noteRepo.findBySeanceId(seanceId);
    }

    // Note l'étudiant en fonction de la séance et du type de note
    public Note noterEtudiant(Note note, SeanceDTO seance) {
        // Récupérer la note existante pour la même combinaison
        Optional<Note> existingNoteOpt = noteRepo.findByEtudiantIdAndGroupeIdAndSeanceId(
                note.getEtudiantId(), note.getGroupeId(), note.getSeanceId());

        if (existingNoteOpt.isPresent()) {
            // Si la note existe déjà, on met à jour la valeur de la note
            Note existingNote = existingNoteOpt.get();
            existingNote.setValeur(note.getValeur()); // Mise à jour de la valeur de la note
            return noteRepo.save(existingNote); // Enregistrement de la note mise à jour
        }

        // Si aucune note n'existe, on insère la nouvelle note
        return noteRepo.save(note);
    }



    // Note un groupe d'étudiants
    public List<Note> noterGroupe(List<Note> notes, List<SeanceDTO> seances) {
        if (seances.isEmpty() || seances.get(0).getTypeNote() != TypeNote.GROUPE) {
            throw new IllegalArgumentException("La séance n'est pas de type 'GROUPE'.");
        }
        return noteRepo.saveAll(notes);
    }

    // Calcule la moyenne d'un étudiant
    public Double calculerMoyenneEtudiant(String etudiantId) {
        List<Note> notes = noteRepo.findByEtudiantId(etudiantId);
        return calculerMoyenne(notes);
    }

    // Calcule la moyenne d'une séance
    public Double calculerMoyenneSeance(String seanceId) {
        List<Note> notes = noteRepo.findBySeanceId(seanceId);
        return calculerMoyenne(notes);
    }

    // Calcule la moyenne d'un sprint
    public Double calculerMoyenneSprint(String sprintId) {
        List<Note> notes = noteRepo.findBySprintId(sprintId);
        return calculerMoyenne(notes);
    }

    // Calcule la moyenne générale des notes
    private Double calculerMoyenne(List<Note> notes) {
        if (notes == null || notes.isEmpty()) return 0.0;
        return notes.stream()
                .mapToDouble(Note::getValeur)
                .average()
                .orElse(0.0);
    }



}
