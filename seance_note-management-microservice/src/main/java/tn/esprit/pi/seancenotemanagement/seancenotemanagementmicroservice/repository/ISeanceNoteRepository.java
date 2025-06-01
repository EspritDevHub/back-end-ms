package tn.esprit.pi.seancenotemanagement.seancenotemanagementmicroservice.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import tn.esprit.pi.seancenotemanagement.seancenotemanagementmicroservice.Entities.SeanceNote;

import java.util.List;

public interface ISeanceNoteRepository extends MongoRepository<SeanceNote, String> {
    // Trouver les séances par sprintId
    List<SeanceNote> findBySprintId(String sprintId);
}