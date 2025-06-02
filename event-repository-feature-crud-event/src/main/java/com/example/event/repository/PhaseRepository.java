package com.example.event.repository;

import com.example.event.model.Phase;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PhaseRepository extends MongoRepository<Phase, String> {

}
