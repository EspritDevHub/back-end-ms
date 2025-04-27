package tn.esprit.pi.documentmanagement.documentmanagementmicroservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import tn.esprit.pi.documentmanagement.documentmanagementmicroservice.Entities.Assignment;

import java.util.List;

public interface IAssignmentRepository extends MongoRepository<Assignment, String> {

    // Récupérer tous les travaux assignés à une séance
    List<Assignment> findBySeanceId(String seanceId);

    // Récupérer tous les travaux assignés à un enseignant
    List<Assignment> findByEnseignantId(String enseignantId);
}

