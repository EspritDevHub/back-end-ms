package com.example.event.repository;

import com.example.event.model.Sprint;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SprintRepository extends MongoRepository<Sprint, String> {
    List<Sprint> findByPhaseId(String phaseId);
}