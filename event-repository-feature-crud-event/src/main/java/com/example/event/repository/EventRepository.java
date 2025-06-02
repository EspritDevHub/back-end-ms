package com.example.event.repository;

import com.example.event.model.Event;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends MongoRepository<Event, String> {
    List<Event> findByTitleContainingIgnoreCase(String title);
    List<Event> findByIsActive(Boolean isActive);
    List<Event> findByStartDateBefore(LocalDateTime dateTime);
    List<Event> findByStartDateAfter(LocalDateTime dateTime);

    List<Event> findByProjectId(String projectId);
}
