package tn.esprit.pi.notemanagement.notemanagementmicroservice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Entities.Note;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Entities.Seance;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Enum.TypeNote;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.repository.INoteRepository;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.repository.ISeanceRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoteService {
    private final INoteRepository noteRepo;
    private final ISeanceRepository iSeanceRepository;


    public List<Note> getNotesParEtudiant(String etudiantId) {
        return noteRepo.findByEtudiantId(etudiantId);
    }

    public List<Note> getNotesParGroupe(String groupeId) {
        return noteRepo.findByGroupeId(groupeId);
    }

    public List<Note> getNotesParSeance(String seanceId) {
        return noteRepo.findBySeanceId(seanceId);
    }

    public Note noterEtudiant(Note note) {
        // Exemple de récupération d'une Seance via le seanceId
        Seance seance = iSeanceRepository.findById(note.getSeanceId()).orElseThrow(() -> new RuntimeException("Séance non trouvée"));

        if (seance.getTypeNote() == TypeNote.INDIVIDUELLE) {
            // Traitement de la note individuelle
            return noteRepo.save(note);
        } else if (seance.getTypeNote() == TypeNote.GROUPE) {
            // Traitement de la note de groupe
            return noteRepo.save(note);
        } else {
            throw new IllegalArgumentException("Le type de note de la séance est inconnu.");
        }
    }
    public List<Note> noterGroupe(List<Note> notes) {
        // Vérifier le type de note pour chaque séance en utilisant seanceId
        notes.forEach(note -> {
            // Récupérer la séance correspondante via seanceId
            Seance seance = iSeanceRepository.findById(note.getSeanceId())
                    .orElseThrow(() -> new IllegalArgumentException("Séance non trouvée avec l'ID: " + note.getSeanceId()));

            // Vérifier le type de note
            if (seance.getTypeNote() != TypeNote.GROUPE) {
                throw new IllegalArgumentException("La séance pour ce groupe n'est pas de type 'GROUPE'");
            }
        });

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
