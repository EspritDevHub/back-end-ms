package tn.esprit.pi.evaluationfeedbackservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import tn.esprit.pi.evaluationfeedbackservice.entity.Critere;
import tn.esprit.pi.evaluationfeedbackservice.entity.Evaluation;

import java.util.List;
import java.util.Optional;

public interface EvaluationRepository extends MongoRepository<Evaluation, String> {
    List<Evaluation> findByProjet(Long projet);
    List<Evaluation> findByProjetAndCritere(Long projet, Critere critere);
    boolean existsByProjet(Long projet);
    Optional<Evaluation> findByProjetAndUserAndCritere(Long projet, Long user, Critere critere);
}
