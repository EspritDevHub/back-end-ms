package tn.esprit.pi.notemanagement.notemanagementmicroservice.services;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Dtos.CritereEvaluationDTO;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Dtos.SeanceDTO;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Dtos.SprintDTO;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Entities.CritereEvaluation;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Feign.SeanceClient;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Feign.SprintClient;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.repository.ICritereEvaluationRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CritereEvaluationService {
    private final ICritereEvaluationRepository critereRepo;
    SeanceClient seanceClient;

    public CritereEvaluation create(CritereEvaluation critere) {
        return critereRepo.save(critere);
    }


    public void affecterCriteresASeance(String seanceId, List<CritereEvaluationDTO> criteres) {
        // Récupérer la séance via le SeanceClient
        SeanceDTO seance = seanceClient.getSeanceById(seanceId);

        // Convertir les critères en entités CritereEvaluation et ajouter leurs IDs à la séance
        List<String> critereIds = criteres.stream()
                .map(CritereEvaluationDTO::getId)  // Récupérer l'ID de chaque critère
                .collect(Collectors.toList());

        // Affecter les critères à la séance
        seance.setCritereIds(critereIds);

        // Mettre à jour la séance via le client Feign
        seanceClient.updateSeance(seanceId, seance);
    }
    public List<CritereEvaluation> getAll() {
        return critereRepo.findAll();
    }

    public Optional<CritereEvaluation> get(String id) {
        return critereRepo.findById(id);
    }

    public CritereEvaluation update(String id, CritereEvaluation critere) {
        critere.setId(id);
        return critereRepo.save(critere);
    }

    public void delete(String id) {
        critereRepo.deleteById(id);
    }

    public void deleteAll() {
        critereRepo.deleteAll();
    }
     SprintClient sprintClient;

    public List<SprintDTO> getAllSprints() {
        return sprintClient.getSprints();
    }

    public List<CritereEvaluation> getCriteriaBySprint(String sprintId) {
        return critereRepo.findBySprintId(sprintId);
    }

}

