package tn.esprit.pi.notemanagement.notemanagementmicroservice.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Entities.CritereEvaluation;

import java.util.List;

public interface ICritereEvaluationRepository extends MongoRepository<CritereEvaluation, String> {
    // Méthode pour trouver tous les critères par Sprint
    List<CritereEvaluation> findBySprintId(String sprintId);
}
