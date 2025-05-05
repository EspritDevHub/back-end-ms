package tn.esprit.pi.seancemanagement.seancemanagementmicroservice.services;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.pi.seancemanagement.seancemanagementmicroservice.Entities.Seance;
import tn.esprit.pi.seancemanagement.seancemanagementmicroservice.repository.ISeanceRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SeanceService {

    @Autowired
    private ISeanceRepository seanceRepository;

    // Créer une nouvelle séance
    public Seance createSeance(Seance seance) {
        return seanceRepository.save(seance);
    }

    // Récupérer toutes les séances d'un sprint spécifique
    public List<Seance> getSeancesBySprintId(String sprintId) {
        return seanceRepository.findBySprintId(sprintId);
    }

    public List<Seance> getAll() {
        return seanceRepository.findAll();
    }

    // Récupérer une séance par son ID
    public Optional<Seance> getSeanceById(String id) {
        return Optional.ofNullable(seanceRepository.findById(id).orElseThrow(() -> new SeanceNotFoundException(id)));

    }


    // Modifier une séance
    public Seance updateSeance(String id, Seance seanceDetails) {
        Optional<Seance> seance = seanceRepository.findById(id);
        if (seance.isPresent()) {
            Seance updatedSeance = seance.get();
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

    public Seance affecterSprint(String seanceId, String sprintId) {
        Seance seance = seanceRepository.findById(seanceId).orElseThrow();
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
