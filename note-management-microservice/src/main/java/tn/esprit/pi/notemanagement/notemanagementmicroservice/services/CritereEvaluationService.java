package tn.esprit.pi.notemanagement.notemanagementmicroservice.services;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
public class CritereEvaluationService {

    @Autowired
    private ICritereEvaluationRepository critereRepo;

    @Qualifier("seanceClient")
    @Autowired
    private SeanceClient seanceClient;
    @Qualifier("sprintClient")
    @Autowired
    private SprintClient sprintClient;

    public CritereEvaluation create(CritereEvaluation critere) {
        return critereRepo.save(critere);
    }

    public Optional<CritereEvaluation> getCritereByName(String name) {
        return critereRepo.findByNom(name);
    }



    public void affecterCriteresASeance(String seanceId, List<String> critereNoms) {
        SeanceDTO seance = seanceClient.getSeanceById(seanceId);

        List<String> critereIds = critereNoms.stream()
                .map(nom -> critereRepo.findByNom(nom).orElseThrow(() ->
                        new RuntimeException("Critère non trouvé: " + nom)))
                .map(CritereEvaluation::getId)
                .collect(Collectors.toList());

        seance.setCritereIds(critereIds);
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

    public List<SprintDTO> getAllSprints() {
        return sprintClient.getSprints();
    }

    public List<CritereEvaluation> getCriteriaBySprint(String sprintId) {
        return critereRepo.findBySprintId(sprintId);
    }
    public void desaffecterCriteresDeSeance(String seanceId, List<String> critereNoms) {
        SeanceDTO seance = seanceClient.getSeanceById(seanceId);

        List<String> critereIdsASupprimer = critereNoms.stream()
                .map(nom -> critereRepo.findByNom(nom).orElseThrow(() ->
                        new RuntimeException("Critère non trouvé: " + nom)))
                .map(CritereEvaluation::getId)
                .collect(Collectors.toList());

        List<String> critereIdsRestants = seance.getCritereIds().stream()
                .filter(id -> !critereIdsASupprimer.contains(id))  // Enlever les critères à supprimer
                .collect(Collectors.toList());

        seance.setCritereIds(critereIdsRestants);

        seanceClient.updateSeance(seanceId, seance);
    }


}

