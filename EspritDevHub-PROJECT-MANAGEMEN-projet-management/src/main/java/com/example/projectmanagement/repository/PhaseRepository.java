package com.example.projectmanagement.repository;


import com.example.projectmanagement.Entities.Phase;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhaseRepository extends MongoRepository<Phase, String> {
}
