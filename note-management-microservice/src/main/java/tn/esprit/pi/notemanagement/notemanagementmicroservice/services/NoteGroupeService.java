package tn.esprit.pi.notemanagement.notemanagementmicroservice.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Entities.NoteGroupe;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.repository.INoteGroupeRepository;

import java.util.List;
import java.util.Optional;

@Service
public class NoteGroupeService {

    @Autowired
    private INoteGroupeRepository noteGroupeRepository;

    // Créer une nouvelle NoteGroupe
    public NoteGroupe createNoteGroupe(NoteGroupe noteGroupe) {
        return noteGroupeRepository.save(noteGroupe);
    }

    // Récupérer toutes les NoteGroupes par séance
    public List<NoteGroupe> getNoteGroupesBySeanceId(String seanceId) {
        return noteGroupeRepository.findAll(); // Ajouter une méthode de recherche par seanceId dans repository si nécessaire
    }

    // Récupérer une NoteGroupe par ID
    public Optional<NoteGroupe> getNoteGroupeById(String id) {
        return noteGroupeRepository.findById(id);
    }

    // Mettre à jour une NoteGroupe
    public NoteGroupe updateNoteGroupe(String id, NoteGroupe noteGroupe) {
        if(noteGroupeRepository.existsById(id)) {
            noteGroupe.setId(id);
            return noteGroupeRepository.save(noteGroupe);
        }
        return null; // Ou lancer une exception
    }

    // Supprimer une NoteGroupe par ID
    public void deleteNoteGroupe(String id) {
        noteGroupeRepository.deleteById(id);
    }
}
