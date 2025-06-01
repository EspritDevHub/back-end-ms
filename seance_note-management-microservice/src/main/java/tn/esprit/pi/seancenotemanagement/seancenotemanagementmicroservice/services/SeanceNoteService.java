package tn.esprit.pi.seancenotemanagement.seancenotemanagementmicroservice.services;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.pi.seancenotemanagement.seancenotemanagementmicroservice.Entities.SeanceNote;
import tn.esprit.pi.seancenotemanagement.seancenotemanagementmicroservice.repository.ISeanceNoteRepository;

import java.util.List;
import java.util.Optional;

@Service
public class SeanceNoteService {

    @Autowired
    private ISeanceNoteRepository seanceRepository;

    // Créer une nouvelle séance
    public SeanceNote createSeance(SeanceNote seance) {
        return seanceRepository.save(seance);
    }

    // Récupérer toutes les séances d'un sprint spécifique
    public List<SeanceNote> getSeancesBySprintId(String sprintId) {
        return seanceRepository.findBySprintId(sprintId);
    }

    public List<SeanceNote> getAll() {
        return seanceRepository.findAll();
    }

    // Récupérer une séance par son ID
    public Optional<SeanceNote> getSeanceById(String id) {
        return Optional.ofNullable(seanceRepository.findById(id).orElseThrow(() -> new SeanceNotFoundException(id)));

    }


    // Modifier une séance
    public SeanceNote updateSeance(String id, SeanceNote seanceDetails) {
        Optional<SeanceNote> seance = seanceRepository.findById(id);
        if (seance.isPresent()) {
            SeanceNote updatedSeance = seance.get();
            updatedSeance.setTitre(seanceDetails.getTitre());
            updatedSeance.setDescription(seanceDetails.getDescription());
            updatedSeance.setNumero(seanceDetails.getNumero());
            updatedSeance.setNote(seanceDetails.getNote());
            updatedSeance.setSprintId(seanceDetails.getSprintId());
            updatedSeance.setCritereIds(seanceDetails.getCritereIds());
            return seanceRepository.save(updatedSeance);
        }
        return null;
    }

    public SeanceNote affecterSprint(String seanceId, String sprintId) {
        SeanceNote seance = seanceRepository.findById(seanceId).orElseThrow();
        seance.setSprintId(sprintId);
        return seanceRepository.save(seance);
    }



    public class SeanceNotFoundException extends RuntimeException {
        public SeanceNotFoundException(String id) {
            super("Seance not found with id: " + id);
        }
    }

    // Supprimer une séance
    public void deleteSeance(String id) {
        seanceRepository.deleteById(id);
    }
}
