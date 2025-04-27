package tn.esprit.pi.notemanagement.notemanagementmicroservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Dtos.CritereEvaluationDTO;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Mappers.CritereEvaluationMapper;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.services.CritereEvaluationService;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Feign.UserClient;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/criteres")
@CrossOrigin(origins = "http://localhost:4200")  // Autorise les requêtes CORS depuis Angular
@RequiredArgsConstructor
public class CritereEvaluationController {

    private final CritereEvaluationService service;
    UserClient userClient; // Injection du UserClient

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
                service.create(CritereEvaluationMapper.toEntity(dto))
        );
    }

    // Récupérer tous les critères (Admin uniquement)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<CritereEvaluationDTO> all(@RequestHeader("userId") String userId) {
        // Vérification du rôle utilisateur via UserClient
        String role = getUserRole(userId);
        if (!"ADMIN".equals(role)) {
            throw new IllegalArgumentException("Accès refusé : utilisateur non autorisé.");
        }

        return service.getAll().stream()
                .map(CritereEvaluationMapper::toDto)
                .collect(Collectors.toList());
    }

    // Mettre à jour un critère (Admin uniquement)
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public CritereEvaluationDTO update(@PathVariable String id, @RequestBody CritereEvaluationDTO dto) {
        return CritereEvaluationMapper.toDto(
                service.update(id, CritereEvaluationMapper.toEntity(dto))
        );
    }

    // Supprimer un critère (Admin uniquement)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.delete(id);
    }

    // Supprimer tous les critères (Admin uniquement)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping()
    public void deleteAll() {
        service.deleteAll();
    }
}
