package tn.esprit.pi.evaluationfeedbackservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pi.evaluationfeedbackservice.entity.Critere;
import tn.esprit.pi.evaluationfeedbackservice.entity.Evaluation;
import tn.esprit.pi.evaluationfeedbackservice.service.EvaluationService;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/evaluations")
@CrossOrigin(origins = "*")
public class EvaluationController {

    private final EvaluationService service;

    public EvaluationController(EvaluationService service) {
        this.service = service;
    }

    @GetMapping
    public List<Evaluation> getAll() {
        List<Evaluation> list = service.getAll();
        return list;
    }

    @PostMapping
    public ResponseEntity<Evaluation> addEvaluation(@RequestBody Evaluation evaluation) {

        Evaluation saved = service.addOrUpdateEvaluation(evaluation);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.deleteEvaluation(id);
    }

    @GetMapping("/by-project")
    public ResponseEntity<?> getEvaluationsByProject(
            @RequestParam String projectId,
            @RequestParam(required = false) Critere critere) {

        try {
            List<Evaluation> evaluations = service.getEvaluationsByProject(projectId, critere);
            return ResponseEntity.ok(evaluations);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/stats")
    public ResponseEntity<?> getEvaluationStats(@RequestParam Long projectId) {
        try {
            return ResponseEntity.ok(service.getAverageStatsByProject(projectId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
}
