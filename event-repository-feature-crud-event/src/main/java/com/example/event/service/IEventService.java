package com.example.event.service;

import com.example.event.model.Event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface IEventService {

    Event add(Event event);
    List<Event> addAll(List<Event> events);
    Event update(Event event);
    List<Event> updateAll(List<Event> events);
    Optional<Event> selectById(String id);
    List<Event> selectAll();
    void delete(Event event);
    void deleteAll(List<Event> events);
    void deleteById(String id);
    void deleteAll();

    List<Event> findByTitle(String title);
    List<Event> findByIsActive(Boolean isActive);
    List<Event> findByStartDateBeforeNow();
    List<Event> findUpcomingEvents();
    List<Event> searchEvents(String keyword, String organizer, LocalDateTime after);
    List<Event> getEventsByDateRange(LocalDateTime start, LocalDateTime end);
    }
