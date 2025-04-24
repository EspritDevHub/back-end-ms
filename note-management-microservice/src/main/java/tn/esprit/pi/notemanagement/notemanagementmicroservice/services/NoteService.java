package tn.esprit.pi.notemanagement.notemanagementmicroservice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Entities.Note;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.repository.INoteRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoteService {
    private final INoteRepository noteRepo;

    public Note noterEtudiant(Note note) {
        return noteRepo.save(note);
    }

    public List<Note> getNotesParEtudiant(String etudiantId) {
        return noteRepo.findByEtudiantId(etudiantId);
    }

    public List<Note> getNotesParGroupe(String groupeId) {
        return noteRepo.findByGroupeId(groupeId);
    }

    public List<Note> getNotesParSeance(String seanceId) {
        return noteRepo.findBySeanceId(seanceId);
    }
    public List<Note> noterGroupe(List<Note> notes) {
        return noteRepo.saveAll(notes); // Sauvegarde de toutes les notes
    }

    public Double calculerMoyenneEtudiant(String etudiantId) {
        List<Note> notes = noteRepo.findByEtudiantId(etudiantId);
        return calculerMoyenne(notes);
    }

    public Double calculerMoyenneSeance(String seanceId) {
        List<Note> notes = noteRepo.findBySeanceId(seanceId);
        return calculerMoyenne(notes);
    }

    public Double calculerMoyenneSprint(String sprintId) {
        List<Note> notes = noteRepo.findBySprintId(sprintId);
        return calculerMoyenne(notes);
    }

    private Double calculerMoyenne(List<Note> notes) {
        if (notes == null || notes.isEmpty()) return 0.0;
        return notes.stream()
                .mapToDouble(Note::getValeur)
                .average()
                .orElse(0.0);
    }

}
