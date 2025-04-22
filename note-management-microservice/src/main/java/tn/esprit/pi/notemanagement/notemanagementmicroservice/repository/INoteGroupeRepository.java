package tn.esprit.pi.notemanagement.notemanagementmicroservice.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Entities.NoteGroupe;

@Repository
public interface INoteGroupeRepository extends MongoRepository<NoteGroupe, String> {
}
