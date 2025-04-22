package tn.esprit.pi.notemanagement.notemanagementmicroservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Entities.NoteIndividuelle;

import java.util.List;
import java.util.Optional;

@Repository
public interface INoteIndividuelleRepository extends MongoRepository<NoteIndividuelle, String> {

    // Recherche par utilisateur et séance
    Optional<NoteIndividuelle> findByUserIdAndSeanceId(String userId, String seanceId);

    // Recherche de toutes les notes d'une séance
    List<NoteIndividuelle> findBySeanceId(String seanceId);
}
