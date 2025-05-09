package tn.esprit.pi.documentmanagement.documentmanagementmicroservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import tn.esprit.pi.documentmanagement.documentmanagementmicroservice.Entities.Assignment;

import java.util.List;

public interface IAssignmentRepository extends MongoRepository<Assignment, String> {

    List<Assignment> findBySeanceId(String seanceId);
    List<Assignment> findByEnseignantId(String enseignantId);

}
