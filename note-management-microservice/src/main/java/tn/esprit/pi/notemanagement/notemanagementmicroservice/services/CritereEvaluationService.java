package tn.esprit.pi.notemanagement.notemanagementmicroservice.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Entities.CritereEvaluation;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.repository.ICritereEvaluationRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CritereEvaluationService {

    @Autowired
    private ICritereEvaluationRepository critereEvaluationRepository;

    @Autowired
    private SprintService sprintService;  // Service pour vérifier si un sprint existe

    // Récupérer tous les critères d'évaluation
    public List<CritereEvaluation> getAllCritereEvaluations() {
        return critereEvaluationRepository.findAll();
    }

    // Récupérer les critères par Sprint
    public List<CritereEvaluation> getCritereEvaluationsBySprintId(String sprintId) {
        return critereEvaluationRepository.findBySprintId(sprintId);
    }

    // Créer un critère d'évaluation
    public CritereEvaluation createCritereEvaluation(CritereEvaluation critereEvaluation) {
        // On vérifie si le Sprint existe
        if (sprintService.getSprintById(critereEvaluation.getSprint().getId()).isEmpty()) {
            throw new IllegalArgumentException("Sprint non trouvé");
        }

        // Vérifier si la somme des scores des critères totalise 100% pour ce sprint
        double totalScore = getTotalScoreForSprint(critereEvaluation.getSprint().getId());
        if (totalScore + critereEvaluation.getScore() > 100) {
            throw new IllegalArgumentException("La somme des scores dépasse 100%");
        }

        return critereEvaluationRepository.save(critereEvaluation);
    }

    // Mettre à jour un critère d'évaluation
    public CritereEvaluation updateCritereEvaluation(CritereEvaluation critereEvaluation) {
        return critereEvaluationRepository.save(critereEvaluation);
    }

    // Supprimer un critère d'évaluation
    public void deleteCritereEvaluation(String id) {
        critereEvaluationRepository.deleteById(id);
    }

    // Calculer le score total pour un sprint
    private double getTotalScoreForSprint(String sprintId) {
        List<CritereEvaluation> criteres = getCritereEvaluationsBySprintId(sprintId);
        return criteres.stream().mapToDouble(CritereEvaluation::getScore).sum();
    }
}
