package tn.esprit.pi.notemanagement.notemanagementmicroservice.services;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Entities.CritereEvaluation;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Entities.Seance;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.repository.ICritereEvaluationRepository;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.repository.ISeanceRepository;

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
        return seanceRepository.findById(id);
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
            return seanceRepository.save(updatedSeance);
        }
        return null;
    }

    public Seance affecterSprint(String seanceId, String sprintId) {
        Seance seance = seanceRepository.findById(seanceId).orElseThrow();
        seance.setSprintId(sprintId);
        return seanceRepository.save(seance);
    }

    @Autowired
    private ICritereEvaluationRepository critereRepository;
    public Seance affecterCriteresParNom(String seanceId, List<String> critereNoms) {
        Seance seance = seanceRepository.findById(seanceId).orElseThrow(() -> new RuntimeException("Séance non trouvée"));
        List<CritereEvaluation> criteres = critereRepository.findByNomIn(critereNoms);
        List<String> critereIds = criteres.stream().map(CritereEvaluation::getId).collect(Collectors.toList());
        seance.setCritereIds(critereIds);
        return seanceRepository.save(seance); // Sauvegarde la séance mise à jour
    }

    // Supprimer une séance
    public void deleteSeance(String id) {
        seanceRepository.deleteById(id);
    }
}
