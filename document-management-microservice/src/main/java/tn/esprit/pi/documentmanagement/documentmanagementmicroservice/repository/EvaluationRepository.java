package tn.esprit.pi.documentmanagement.documentmanagementmicroservice.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import tn.esprit.pi.documentmanagement.documentmanagementmicroservice.Entities.Evaluation;

import java.util.List;

public interface EvaluationRepository extends MongoRepository<Evaluation, String> {
    List<Evaluation> findByDocumentId(String documentId);
    List<Evaluation> findByEnseignantId(String enseignantId);
}
