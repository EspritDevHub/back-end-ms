package tn.esprit.pi.notemanagement.notemanagementmicroservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tn.esprit.pi.notemanagement.notemanagementmicroservice.Dtos.CritereEvaluationRequestDTO;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Dtos.CritereEvaluationResponseDTO;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.services.CritereEvaluationService;

import java.util.List;

@RestController
@RequestMapping("/api/criteres")
@RequiredArgsConstructor
public class CritereEvaluationController {

    private final CritereEvaluationService service;

    @GetMapping("/sprint/{sprintId}")
    public List<CritereEvaluationResponseDTO> getBySprint(@PathVariable String sprintId) {
        return service.getBySprintId(sprintId);
    }

    @PostMapping
    public CritereEvaluationResponseDTO create(@RequestBody CritereEvaluationRequestDTO dto) {
        return service.create(dto);
    }

    @GetMapping
    public List<CritereEvaluationResponseDTO> all() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CritereEvaluationResponseDTO> get(@PathVariable String id) {
        return service.get(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public CritereEvaluationResponseDTO update(@PathVariable String id, @RequestBody CritereEvaluationRequestDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/all")
    public void deleteAll() {
        service.deleteAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.delete(id);
    }
}
