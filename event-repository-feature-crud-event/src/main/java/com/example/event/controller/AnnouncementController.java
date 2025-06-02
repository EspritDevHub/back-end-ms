package com.example.event.controller;

import com.example.event.model.Announcement;
import com.example.event.service.IAnnouncementService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/announcements")
@AllArgsConstructor
public class AnnouncementController {

    private final IAnnouncementService announcementService;

    @PostMapping("/add")
    public ResponseEntity<Announcement> add(@RequestBody Announcement announcement) {
        return ResponseEntity.ok(announcementService.add(announcement));
    }

    @PutMapping("/update")
    public ResponseEntity<Announcement> update(@RequestBody Announcement announcement) {
        return ResponseEntity.ok(announcementService.update(announcement));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        announcementService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<Announcement>> getAll() {
        return ResponseEntity.ok(announcementService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Announcement> getById(@PathVariable String id) {
        return announcementService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
