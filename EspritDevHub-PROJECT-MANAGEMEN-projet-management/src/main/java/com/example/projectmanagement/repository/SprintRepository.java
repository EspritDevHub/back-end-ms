package com.example.projectmanagement.repository;

import com.example.projectmanagement.Entities.Phase;
import com.example.projectmanagement.Entities.Sprint;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SprintRepository extends MongoRepository<Sprint, String> {
}

