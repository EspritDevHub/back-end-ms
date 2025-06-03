package com.example.event.controller;

import com.example.event.model.Event;
import com.example.event.service.EventServiceImpl;
import com.example.event.service.IEventService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@AllArgsConstructor
@RequestMapping("/api/event")
public class EventController {

    IEventService iEventService;
    EventServiceImpl eventService;

    @GetMapping("/getAll")
    public List<Event> getAllEvents() {
        return iEventService.selectAll();
    }

    @PostMapping("/add")
    public Event addEvent(@RequestBody Event event) {
        return iEventService.add(event);
    }

    @PostMapping("/addAll")
    public List<Event> addEvents(@RequestBody List<Event> events) {
        return iEventService.addAll(events);
    }

    @GetMapping("/findById")
    public Optional<Event> findById(@RequestParam String id) {
        return iEventService.selectById(id);
    }

    @GetMapping("/get/{id}")
    public Optional<Event> getById(@PathVariable String id) {
        return iEventService.selectById(id);
    }

    @PutMapping("/update/{id}")
    public Event updateEvent(@PathVariable String id, @RequestBody Event event) {
        event.setId(id);
        return iEventService.update(event);
    }


    @DeleteMapping("/delete/{id}")
    public void deleteById(@PathVariable String id) {
        iEventService.deleteById(id);
    }

    @DeleteMapping("/delete")
    public void deleteEvent(@RequestBody Event event) {
        iEventService.delete(event);
    }

    @DeleteMapping("/deleteAll")
    public void deleteAllEvents(@RequestBody List<Event> events) {
        iEventService.deleteAll(events);
    }

    @DeleteMapping("/deleteAllEvents")
    public void deleteAll() {
        iEventService.deleteAll();
    }

    @PostMapping("/projects/{projectId}/events")
    public ResponseEntity<Event> createEventForProject(@PathVariable String projectId, @RequestBody Event event) {
        event.setProjectId(projectId);
        return ResponseEntity.ok(eventService.add(event));
    }
    @PutMapping("/projects/{projectId}/events/{eventId}")
    public ResponseEntity<Event> updateEventForProject(
            @PathVariable String projectId,
            @PathVariable String eventId,
            @RequestBody Event updatedEvent) {

        Event event = eventService.selectById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + eventId));
        if (event == null || !projectId.equals(event.getProjectId())) {
            return ResponseEntity.notFound().build();
        }

        event.setTitle(updatedEvent.getTitle());
        event.setDescription(updatedEvent.getDescription());
        event.setStartDate(updatedEvent.getStartDate());
        event.setEndDate(updatedEvent.getEndDate());
        event.setEventType(updatedEvent.getEventType());
        event.setLocation(updatedEvent.getLocation());
        event.setOrganizer(updatedEvent.getOrganizer());
        event.setUpdatedAt(LocalDateTime.now());

        return ResponseEntity.ok(eventService.update(event));
    }

    @GetMapping("/projects/{projectId}/events")
    public ResponseEntity<List<Event>> getEventsByProject(@PathVariable String projectId) {
        return ResponseEntity.ok(eventService.findByProjectId(projectId));
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<Event>> getUpcomingEvents() {
        return ResponseEntity.ok(eventService.getUpcomingEvents());
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<Event>> getEventsByDateRange(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(eventService.getEventsByDateRange(start, end));
    }


}
