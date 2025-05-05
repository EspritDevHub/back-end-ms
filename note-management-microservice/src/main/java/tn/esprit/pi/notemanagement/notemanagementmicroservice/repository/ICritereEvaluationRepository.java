package tn.esprit.pi.notemanagement.notemanagementmicroservice.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Entities.CritereEvaluation;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Entities.Note;

import java.util.List;
import java.util.Optional;

public interface ICritereEvaluationRepository extends MongoRepository<CritereEvaluation, String> {
    // Méthode pour trouver tous les critères par Sprint
    List<CritereEvaluation> findByNomIn(List<String> noms);
    List<CritereEvaluation> findBySprintId(String sprintId);
    // In ICritereEvaluationRepository
    Optional<CritereEvaluation> findByNom(String name);

}
