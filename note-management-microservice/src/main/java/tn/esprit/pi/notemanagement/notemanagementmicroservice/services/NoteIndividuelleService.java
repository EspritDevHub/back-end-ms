package tn.esprit.pi.notemanagement.notemanagementmicroservice.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Entities.NoteIndividuelle;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.repository.INoteIndividuelleRepository;

import java.util.List;
import java.util.Optional;

@Service
public class NoteIndividuelleService {

    private final INoteIndividuelleRepository noteIndividuelleRepository;

    @Autowired
    public NoteIndividuelleService(INoteIndividuelleRepository noteIndividuelleRepository) {
        this.noteIndividuelleRepository = noteIndividuelleRepository;
    }

    // Ajouter ou mettre à jour une note individuelle
    public NoteIndividuelle saveNoteIndividuelle(NoteIndividuelle noteIndividuelle) {
        return noteIndividuelleRepository.save(noteIndividuelle);
    }

    // Obtenir une note individuelle par utilisateur et séance
    public Optional<NoteIndividuelle> getNoteIndividuelle(String userId, String seanceId) {
        return noteIndividuelleRepository.findByUserIdAndSeanceId(userId, seanceId);
    }

    // Obtenir toutes les notes pour une séance donnée
    public List<NoteIndividuelle> getNotesBySeance(String seanceId) {
        return noteIndividuelleRepository.findBySeanceId(seanceId);
    }
}
