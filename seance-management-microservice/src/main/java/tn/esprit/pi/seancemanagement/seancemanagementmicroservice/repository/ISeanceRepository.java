package tn.esprit.pi.seancemanagement.seancemanagementmicroservice.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import tn.esprit.pi.seancemanagement.seancemanagementmicroservice.Entities.Seance;

import java.time.LocalDate;
import java.util.List;

public interface ISeanceRepository extends MongoRepository<Seance, String> {
    // Trouver les séances par sprintId
    List<Seance> findBySprintId(String sprintId);
    List<Seance> findByDateBetween(LocalDate start, LocalDate end);

}