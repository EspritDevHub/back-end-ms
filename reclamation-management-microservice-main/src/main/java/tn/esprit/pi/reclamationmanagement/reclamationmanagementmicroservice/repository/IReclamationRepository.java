package tn.esprit.pi.reclamationmanagement.reclamationmanagementmicroservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.pi.reclamationmanagement.reclamationmanagementmicroservice.Entities.Reclamation;
@Repository
public interface IReclamationRepository extends MongoRepository <Reclamation, String>
{
}
