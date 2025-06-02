package com.example.event.service;

import com.example.event.model.Event;
import com.example.event.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements IEventService {

    private final EventRepository eventRepository;

    public EventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public Event add(Event event) {
        event.setCreatedAt(LocalDateTime.now());
        event.setUpdatedAt(LocalDateTime.now());
        if (event.getIsActive() == null) {
            event.setIsActive(true);
        }
        return eventRepository.save(event);
    }

    @Override
    public List<Event> addAll(List<Event> events) {
        events.forEach(e -> {
            e.setCreatedAt(LocalDateTime.now());
            e.setUpdatedAt(LocalDateTime.now());
            if (e.getIsActive() == null) {
                e.setIsActive(true);
            }
        });
        return eventRepository.saveAll(events);
    }

    @Override
    public Event update(Event event) {
        event.setUpdatedAt(LocalDateTime.now());
        return eventRepository.save(event);
    }

    @Override
    public List<Event> updateAll(List<Event> events) {
        events.forEach(e -> e.setUpdatedAt(LocalDateTime.now()));
        return eventRepository.saveAll(events);
    }

    @Override
    public Optional<Event> selectById(String id) {
        return eventRepository.findById(id);
    }

    @Override
    public List<Event> selectAll() {
        return eventRepository.findAll();
    }

    @Override
    public void delete(Event event) {
        eventRepository.delete(event);
    }

    @Override
    public void deleteAll(List<Event> events) {
        eventRepository.deleteAll(events);
    }

    @Override
    public void deleteById(String id) {
        eventRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        eventRepository.deleteAll();
    }

    @Override
    public List<Event> findByTitle(String title) {
        return eventRepository.findByTitleContainingIgnoreCase(title);
    }

    @Override
    public List<Event> findByIsActive(Boolean isActive) {
        return eventRepository.findByIsActive(isActive);
    }

    @Override
    public List<Event> findByStartDateBeforeNow() {
        return eventRepository.findByStartDateBefore(LocalDateTime.now());
    }

    @Override
    public List<Event> findUpcomingEvents() {
        return eventRepository.findByStartDateAfter(LocalDateTime.now());
    }

    public List<Event> findByProjectId(String projectId) {
        return eventRepository.findByProjectId(projectId);
    }

    public List<Event> searchEvents(String keyword, String organizer, LocalDateTime after) {
        return eventRepository.findAll().stream()
                .filter(e -> (keyword == null || e.getTitle().contains(keyword) || e.getDescription().contains(keyword)) &&
                        (organizer == null || e.getOrganizer().equalsIgnoreCase(organizer)) &&
                        (after == null || e.getStartDate().isAfter(after)))
                .collect(Collectors.toList());
    }

    public List<Event> getUpcomingEvents() {
        LocalDateTime now = LocalDateTime.now();
        return eventRepository.findAll().stream()
                .filter(event -> event.getStartDate() != null && event.getStartDate().isAfter(now))
                .toList();
    }
    public List<Event> getEventsByDateRange(LocalDateTime start, LocalDateTime end) {
        return eventRepository.findAll().stream()
                .filter(e -> e.getStartDate() != null && !e.getStartDate().isBefore(start) && !e.getStartDate().isAfter(end))
                .toList();
    }


}
