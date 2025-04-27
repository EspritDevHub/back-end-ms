package tn.esprit.pi.notemanagement.notemanagementmicroservice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Dtos.CritereEvaluationDTO;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Dtos.SeanceDTO;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Entities.CritereEvaluation;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Entities.Note;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Enum.TypeNote;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Feign.SeanceClient;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Mappers.CritereEvaluationMapper;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.repository.INoteRepository;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class NoteService {
    private final INoteRepository noteRepo;
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
        if (seance.getTypeNote() == TypeNote.INDIVIDUELLE) {
            return noteRepo.save(note); // Traitement de la note individuelle
        } else if (seance.getTypeNote() == TypeNote.GROUPE) {
            return noteRepo.save(note); // Traitement de la note de groupe
        } else {
            throw new IllegalArgumentException("Le type de note de la séance est inconnu.");
        }
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
