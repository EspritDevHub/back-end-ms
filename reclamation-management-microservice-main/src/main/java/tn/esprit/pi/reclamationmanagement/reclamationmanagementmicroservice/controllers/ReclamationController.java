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
import tn.esprit.pi.reclamationmanagement.reclamationmanagementmicroservice.Enums.ReclamationStatus;
import tn.esprit.pi.reclamationmanagement.reclamationmanagementmicroservice.services.JiraService;
import tn.esprit.pi.reclamationmanagement.reclamationmanagementmicroservice.services.ReclamationService;

import java.util.List;
import java.util.Optional;
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/reclamations")

@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})

public class ReclamationController {

    private final ReclamationService reclamationService;
    private final JiraService jiraService;


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
    @PostMapping("/{id}/jira")
    public ResponseEntity<String> createJiraTicket(@PathVariable String id) {
        ReclamationResponseDTO dto = reclamationService.getById(id);

        // Construction de l'entité Reclamation depuis le DTO
        Reclamation reclamation = new Reclamation();
        reclamation.setId(dto.getId());
        reclamation.setTitle(dto.getTitle());
        reclamation.setDescription(dto.getDescription());
        reclamation.setUserId(dto.getUserId()); // ajoute ce champ si disponible dans DTO

        // Création du ticket Jira
        String jiraKey = jiraService.createIssueFromReclamation(reclamation);

        if (jiraKey != null) {
            // Mise à jour de la réclamation avec le ticket Jira
            reclamationService.saveJiraTicketId(id, jiraKey); // Cette méthode doit être ajoutée dans ton service
            return ResponseEntity.ok("Jira Ticket Created: " + jiraKey);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create Jira ticket");
        }
    }

    @PostMapping("/jira/sync")
    public ResponseEntity<String> syncJiraStatuses() {
        jiraService.checkAndUpdateResolvedTickets();
        return ResponseEntity.ok("Jira status synchronization triggered.");
    }
    @GetMapping("/resolved")
    public List<ReclamationResponseDTO> getResolved() {
        return reclamationService.getAll().stream()
                .filter(r -> ReclamationStatus.RESOLVED.equals(r.getStatus()))
                .toList();
    }
}
