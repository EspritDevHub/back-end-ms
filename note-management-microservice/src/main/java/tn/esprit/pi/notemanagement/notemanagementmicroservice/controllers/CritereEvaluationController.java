package tn.esprit.pi.notemanagement.notemanagementmicroservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Dtos.CritereEvaluationDTO;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Mappers.CritereEvaluationMapper;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.services.CritereEvaluationService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/criteres")
@RequiredArgsConstructor
public class CritereEvaluationController {

    private final CritereEvaluationService service;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public CritereEvaluationDTO create(@RequestBody CritereEvaluationDTO dto) {
        return CritereEvaluationMapper.toDto(
                service.create(CritereEvaluationMapper.toEntity(dto))
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<CritereEvaluationDTO> all() {
        return service.getAll().stream()
                .map(CritereEvaluationMapper::toDto)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public CritereEvaluationDTO update(@PathVariable String id, @RequestBody CritereEvaluationDTO dto) {
        return CritereEvaluationMapper.toDto(
                service.update(id, CritereEvaluationMapper.toEntity(dto))
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.delete(id);
    }
}
