package tn.esprit.pi.notemanagement.notemanagementmicroservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Dtos.CoursDTO;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Entities.Cours;

import java.util.List;

public interface CoursRepository extends MongoRepository<Cours, String> {

    List<Cours> findByMatiereContainingIgnoreCaseAndNiveau(String matiere, String niveau);

}
