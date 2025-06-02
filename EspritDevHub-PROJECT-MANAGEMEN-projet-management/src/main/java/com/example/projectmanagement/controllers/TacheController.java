package com.example.projectmanagement.controllers;


import com.example.projectmanagement.Dtos.TacheDTO;
import com.example.projectmanagement.Entities.Tache;
import com.example.projectmanagement.iservices.ITacheService;
import com.example.projectmanagement.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/taches")
@RequiredArgsConstructor
public class TacheController {

    private final ITacheService tacheService;
    private final NotificationService notificationService;

    @GetMapping("/projet/{projetId}")
    public ResponseEntity<List<TacheDTO>> getTachesByProjet(@PathVariable String projetId) {
        List<TacheDTO> taches = tacheService.getTachesByProjet(projetId);

        if (taches.isEmpty()) {
            // Si aucune tâche n'est trouvée, on renvoie une réponse avec un code 404
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(List.of()); // Ou tu peux renvoyer un message d'erreur
        }

        return ResponseEntity.ok(taches);
    }

    @GetMapping("/taches/statistiques")
    public ResponseEntity<Map<String, Double>> getTachesStatistiques() {
        Map<String, Double> stats = tacheService.getTachesStats();
        return ResponseEntity.ok(stats);
    }


    // Création d'une tâche
    @PostMapping
    public ResponseEntity<?> createTache(@RequestBody TacheDTO tacheDTO) {
        try {
            TacheDTO createdTache = tacheService.createTache(tacheDTO);
            return new ResponseEntity<>(createdTache, HttpStatus.CREATED);
        } catch (HttpMessageConversionException ex) {
            return new ResponseEntity<>("Erreur lors de la conversion des données", HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<>("Erreur serveur: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    // Mise à jour d'une tâche
    @PutMapping("/{id}")
    public ResponseEntity<TacheDTO> updateTache(@PathVariable String id, @RequestBody TacheDTO tacheDTO) {
        TacheDTO updatedTache = tacheService.updateTache(id, tacheDTO);
        return new ResponseEntity<>(updatedTache, HttpStatus.OK);
    }

    // Récupérer une tâche par son ID
    @GetMapping("/{id}")
    public ResponseEntity<TacheDTO> getTacheById(@PathVariable String id) {
        TacheDTO tacheDTO = tacheService.getTacheById(id);
        return new ResponseEntity<>(tacheDTO, HttpStatus.OK);
    }

    // Récupérer toutes les tâches
    @GetMapping
    public ResponseEntity<List<TacheDTO>> getAllTaches() {
        List<TacheDTO> taches = tacheService.getAllTaches();
        return new ResponseEntity<>(taches, HttpStatus.OK);
    }


    // Supprimer une tâche
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTache(@PathVariable String id) {
        tacheService.deleteTache(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    // Endpoint de test pour envoi email via Swagger
    @GetMapping("/test-email")
    public ResponseEntity<String> testEmail(@RequestParam String to) {
        try {
            notificationService.sendEmail(to, "Test envoi email", "Ceci est un test d'envoi d'email.",false);
            return ResponseEntity.ok("Email envoyé à " + to);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur: " + e.getMessage());
        }
    }
    @PostMapping("/ajouter")
    public ResponseEntity<Tache> ajouterTacheAuProjet(@RequestBody TacheDTO tacheDTO) {
        Tache nouvelleTache = tacheService.ajouterTacheAuProjet(tacheDTO);
        return new ResponseEntity<>(nouvelleTache, HttpStatus.CREATED);
    }


}
