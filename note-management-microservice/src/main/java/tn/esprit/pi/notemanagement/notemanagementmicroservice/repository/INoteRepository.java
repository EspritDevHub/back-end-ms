package tn.esprit.pi.notemanagement.notemanagementmicroservice.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Entities.Note;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Entities.Note;

import java.util.List;
import java.util.Optional;

public  interface INoteRepository extends MongoRepository<Note, String> {

    List<Note> findByEtudiantId(String etudiantId);
    List<Note> findByGroupeId(String groupeId);
    List<Note> findBySeanceId(String seanceId);
    List<Note> findBySprintId(String sprintId); // pour accéder à note.seance.sprint.id
    Optional<Note> findByEtudiantIdAndGroupeIdAndSeanceId(String etudiantId, String groupeId, String seanceId);

}