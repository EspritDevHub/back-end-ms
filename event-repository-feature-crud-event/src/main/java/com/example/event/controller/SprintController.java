package com.example.event.controller;

import com.example.event.dto.SprintDTO;
import com.example.event.mapper.SprintMapper;
import com.example.event.model.Sprint;
import com.example.event.service.ISprintService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sprints")
@RequiredArgsConstructor
public class SprintController {

    private final ISprintService sprintService;

    @PostMapping
    public ResponseEntity<SprintDTO> createSprint(@RequestBody SprintDTO sprintDTO) {
        Sprint saved = sprintService.create(SprintMapper.toEntity(sprintDTO));
        return ResponseEntity.ok(SprintMapper.toDTO(saved));
    }

    @GetMapping
    public ResponseEntity<List<SprintDTO>> getAllSprints() {
        List<Sprint> sprints = sprintService.getAll();
        return ResponseEntity.ok(SprintMapper.toDTOList(sprints));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SprintDTO> getSprintById(@PathVariable String id) {
        return sprintService.getById(id)
                .map(sprint -> ResponseEntity.ok(SprintMapper.toDTO(sprint)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<SprintDTO> updateSprint(@PathVariable String id, @RequestBody SprintDTO sprintDTO) {
        Sprint existing = sprintService.getById(id)
                .orElseThrow(() -> new RuntimeException("Sprint not found"));

        existing.setTitle(sprintDTO.getTitle());
        existing.setPhaseId(sprintDTO.getPhaseId());
        existing.setStartDate(sprintDTO.getStartDate());
        existing.setEndDate(sprintDTO.getEndDate());
        existing.setStatus(sprintDTO.getStatus());
        existing.setIsActive(sprintDTO.getActive());

        Sprint updated = sprintService.update(existing);
        return ResponseEntity.ok(SprintMapper.toDTO(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSprint(@PathVariable String id) {
        sprintService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-phase/{phaseId}")
    public ResponseEntity<List<SprintDTO>> getSprintsByPhaseId(@PathVariable String phaseId) {
        List<Sprint> sprints = sprintService.getByPhaseId(phaseId);
        return ResponseEntity.ok(SprintMapper.toDTOList(sprints));
    }

    @GetMapping("/current")
    public ResponseEntity<List<Sprint>> getCurrentSprints() {
        return ResponseEntity.ok(sprintService.getCurrentSprints());
    }

}