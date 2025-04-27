package tn.esprit.pi.notemanagement.notemanagementmicroservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Dtos.CritereEvaluationDTO;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Dtos.SeanceDTO;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Dtos.SprintDTO;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Entities.CritereEvaluation;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Feign.SeanceClient;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Feign.SprintClientFallback;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Mappers.CritereEvaluationMapper;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.services.CritereEvaluationService;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Feign.UserClient;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/criteres")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class CritereEvaluationController {

    private final CritereEvaluationService CritereEvaluationService;
    UserClient userClient; // Injection du UserClient
SeanceClient seanceClient;
    @Autowired
    private SprintClientFallback SprintClientFallback;

    // Fonction pour récupérer le rôle de l'utilisateur
    private String getUserRole(String userId) {
        try {
            // Appel au UserClient pour récupérer le rôle de l'utilisateur
            return userClient.getUserRole(userId);
        } catch (Exception e) {
            return "UNKNOWN"; // Retour par défaut en cas d'erreur
        }
    }

    // Créer un critère (Admin uniquement)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public CritereEvaluationDTO create(@RequestBody CritereEvaluationDTO dto) {
        return CritereEvaluationMapper.toDto(
                CritereEvaluationService.create(CritereEvaluationMapper.toEntity(dto))
        );
    }

    // Récupérer tous les critères (Admin uniquement)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<CritereEvaluationDTO> all() {
        // Vérification du rôle utilisateur via UserClient
        //   String role = getUserRole(userId);
        // if (!"ADMIN".equals(role)) {
        //   throw new IllegalArgumentException("Accès refusé : utilisateur non autorisé.");
        //}

        return CritereEvaluationService.getAll().stream()
                .map(CritereEvaluationMapper::toDto)
                .collect(Collectors.toList());
    }

    // Mettre à jour un critère (Admin uniquement)
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public CritereEvaluationDTO update(@PathVariable String id, @RequestBody CritereEvaluationDTO dto) {
        return CritereEvaluationMapper.toDto(
                CritereEvaluationService.update(id, CritereEvaluationMapper.toEntity(dto))
        );
    }

    // Supprimer un critère (Admin uniquement)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        CritereEvaluationService.delete(id);
    }

    // Supprimer tous les critères (Admin uniquement)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping()
    public void deleteAll() {
        CritereEvaluationService.deleteAll();
    }

    @GetMapping("/sprints/{sprintId}")
    public List<CritereEvaluationDTO> getCriteriaBySprint(@PathVariable String sprintId) {
        // Appel au service pour récupérer les critères associés au sprint
        List<CritereEvaluation> criteres = CritereEvaluationService.getCriteriaBySprint(sprintId);
        return criteres.stream()
                .map(CritereEvaluationMapper::toDto)
                .collect(Collectors.toList());
    }


    @GetMapping("/sprints")
    public List<SprintDTO> getAllSprints() {
        return SprintClientFallback.getSprints();
    }


    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/affecter-criteres/{seanceId}")
    public ResponseEntity<Void> affecterCriteresASeance(@PathVariable String seanceId, @RequestBody List<CritereEvaluationDTO> criteres) {
        // Récupérer la séance par ID via SeanceClient
        SeanceDTO seance = seanceClient.getSeanceById(seanceId);
        if (seance == null) {
            return ResponseEntity.notFound().build(); // Retourner une réponse Not Found si la séance n'existe pas
        }

        // Affecter les critères à la séance
        CritereEvaluationService.affecterCriteresASeance(seanceId, criteres);

        return ResponseEntity.ok().build(); // Retourner un statut OK une fois l'affectation effectuée
    }

}
