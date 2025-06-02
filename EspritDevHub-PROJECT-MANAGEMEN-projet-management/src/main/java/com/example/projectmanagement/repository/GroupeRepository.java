package com.example.projectmanagement.repository;

import com.example.projectmanagement.Entities.Groupe;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupeRepository extends MongoRepository<Groupe, String>{
}
