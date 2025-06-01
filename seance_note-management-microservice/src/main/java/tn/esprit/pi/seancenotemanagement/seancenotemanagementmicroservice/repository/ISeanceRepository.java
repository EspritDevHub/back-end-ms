package tn.esprit.pi.seancenotemanagement.seancenotemanagementmicroservice.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import tn.esprit.pi.seancenotemanagement.seancenotemanagementmicroservice.Entities.Seance;

import java.util.List;

public interface ISeanceRepository extends MongoRepository<Seance, String> {
    // Trouver les séances par sprintId
    List<Seance> findBySprintId(String sprintId);
}