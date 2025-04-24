package tn.esprit.pi.notemanagement.notemanagementmicroservice.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Entities.CritereEvaluation;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.services.CritereEvaluationService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/criteres")
@RequiredArgsConstructor
public class CritereEvaluationController {
    private final CritereEvaluationService service;

    @PostMapping
    public CritereEvaluation create(@RequestBody CritereEvaluation c) {
        return service.create(c);
    }

    @GetMapping
    public List<CritereEvaluation> all() {
        return service.getAll();
    }

    @PutMapping("/{id}")
    public CritereEvaluation update(@PathVariable String id, @RequestBody CritereEvaluation c) {
        return service.update(id, c);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.delete(id);
    }
}