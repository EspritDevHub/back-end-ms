package tn.esprit.pi.reclamationmanagement.reclamationmanagementmicroservice.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;
import tn.esprit.pi.reclamationmanagement.reclamationmanagementmicroservice.Dto.ReclamationRequestDTO;
import tn.esprit.pi.reclamationmanagement.reclamationmanagementmicroservice.Dto.ReclamationResponseDTO;
import tn.esprit.pi.reclamationmanagement.reclamationmanagementmicroservice.Entities.Reclamation;
import tn.esprit.pi.reclamationmanagement.reclamationmanagementmicroservice.services.ReclamationService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reclamations")

@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})

public class ReclamationController {
    @Autowired

    private final ReclamationService reclamationService;

    @Autowired
    public ReclamationController(ReclamationService reclamationService) {
        this.reclamationService = reclamationService;
    }

   /* @PostMapping
    public ResponseEntity<Reclamation> createReclamation(@RequestBody Reclamation reclamation) {
        Reclamation savedReclamation = reclamationService.createReclamation(reclamation);
        return new ResponseEntity<>(savedReclamation, HttpStatus.CREATED);


    @GetMapping
    public ResponseEntity<List<Reclamation>> getAllReclamations() {
        List<Reclamation> reclamations = reclamationService.getAllReclamations();
        return new ResponseEntity<>(reclamations, HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Reclamation> getReclamationById(@PathVariable String id) {
        Optional<Reclamation> reclamation = reclamationService.getReclamationById(id);
        return reclamation.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReclamation(@PathVariable String id) {
        try {
            reclamationService.deleteReclamation(id);
            return ResponseEntity.noContent().build(); // Returns 204 No Content
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<Reclamation> updateReclamation(@PathVariable String id,
                                                         @RequestBody Reclamation reclamationDetails) {
        Reclamation updatedReclamation = reclamationService.updateReclamation(id, reclamationDetails);
        return ResponseEntity.ok(updatedReclamation);
    }
    */
   @PostMapping("/{userId}")
   public ReclamationResponseDTO create(@PathVariable String userId,
                                        @Valid @RequestBody ReclamationRequestDTO dto) {
       return reclamationService.createReclamation(userId, dto);
   }
    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id, @RequestParam String userId) {
        reclamationService.deleteReclamation(id, userId);
    }    @GetMapping("/{id}")
    public ReclamationResponseDTO getById(@PathVariable String id) {
        return reclamationService.getById(id);
    }
    @GetMapping
    public List<ReclamationResponseDTO> getAll() {
        return reclamationService.getAll();
    }

    @PutMapping("/{id}")
    public ReclamationResponseDTO update(@PathVariable String id,
                                         @Valid @RequestBody ReclamationRequestDTO dto) {
        return reclamationService.update(id, dto);
    }
}
