package com.example.event.controller;

import com.example.event.dto.PhaseDTO;
import com.example.event.mapper.PhaseMapper;
import com.example.event.model.Phase;
import com.example.event.service.IPhaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/phases")
@RequiredArgsConstructor
public class PhaseController {

    private final IPhaseService phaseService;

    @PostMapping
    public ResponseEntity<PhaseDTO> createPhase(@RequestBody PhaseDTO phaseDTO) {
        Phase saved = phaseService.create(PhaseMapper.toEntity(phaseDTO));
        return ResponseEntity.ok(PhaseMapper.toDTO(saved));
    }

    @GetMapping
    public ResponseEntity<List<PhaseDTO>> getAllPhases() {
        List<Phase> phases = phaseService.getAll();
        return ResponseEntity.ok(PhaseMapper.toDTOList(phases));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PhaseDTO> getPhaseById(@PathVariable String id) {
        return phaseService.getById(id)
                .map(phase -> ResponseEntity.ok(PhaseMapper.toDTO(phase)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PhaseDTO> updatePhase(@PathVariable String id, @RequestBody PhaseDTO phaseDTO) {
        Phase existing = phaseService.getById(id)
                .orElseThrow(() -> new RuntimeException("Phase not found"));

        existing.setName(phaseDTO.getName());
        existing.setDescription(phaseDTO.getDescription());
        existing.setStartDate(phaseDTO.getStartDate());
        existing.setEndDate(phaseDTO.getEndDate());
        existing.setIsActive(phaseDTO.getIsActive());

        Phase updated = phaseService.update(existing);
        return ResponseEntity.ok(PhaseMapper.toDTO(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePhase(@PathVariable String id) {
        phaseService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{phaseId}/progress")
    public ResponseEntity<Double> getPhaseProgress(@PathVariable String phaseId) {
        double progress = phaseService.getPhaseProgress(phaseId);
        return ResponseEntity.ok(progress);
    }

    @GetMapping("/dashboard-summary")
    public ResponseEntity<Map<String, Object>> getDashboardSummary() {
        Map<String, Object> summary = phaseService.getDashboardSummary();
        return ResponseEntity.ok(summary);
    }

}
