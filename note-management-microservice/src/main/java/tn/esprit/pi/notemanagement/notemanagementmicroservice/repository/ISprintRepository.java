package tn.esprit.pi.notemanagement.notemanagementmicroservice.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Entities.Sprint;

public  interface ISprintRepository  extends MongoRepository<Sprint, String> {
}
