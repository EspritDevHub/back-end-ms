package com.example.projectmanagement.controllers;

import com.example.projectmanagement.Dtos.ProjetDTO;
import com.example.projectmanagement.Dtos.TacheDTO;
import com.example.projectmanagement.Entities.Enums.EtatProjetEnum;
import com.example.projectmanagement.Entities.Groupe;
import com.example.projectmanagement.Entities.Projet;
import com.example.projectmanagement.iservices.IProjetService;
import com.example.projectmanagement.repository.GroupeRepository;
import com.example.projectmanagement.services.KanbanProjetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/projets")
public class ProjetController {

    @Autowired
    private IProjetService projetService;
    @Autowired
    private KanbanProjetService kanbanProjetService;

    @Autowired
    private GroupeRepository groupeRepository;
    // Création d'un projet
    @PostMapping
    public ResponseEntity<ProjetDTO> createProjet(@RequestBody ProjetDTO projetDTO) {
        ProjetDTO createdProjet = projetService.createProjet(projetDTO);
        return new ResponseEntity<>(createdProjet, HttpStatus.CREATED);
    }

    @PostMapping("/addGroupe")
    public ResponseEntity<Groupe> createGroupe(@RequestBody Groupe groupe) {
        return new ResponseEntity<>(groupeRepository.save(groupe), HttpStatus.CREATED);
    }

    // Récupération d'un projet par son ID
    @GetMapping("/{id}")
    public ResponseEntity<ProjetDTO> getProjetById(@PathVariable String id) {
        ProjetDTO projetDTO = projetService.getProjetById(id);
        if (projetDTO != null) {
            return new ResponseEntity<>(projetDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Récupération de tous les projets
    @GetMapping
    public ResponseEntity<List<ProjetDTO>> getAllProjets() {
        List<ProjetDTO> projets = projetService.getAllProjets();
        return new ResponseEntity<>(projets, HttpStatus.OK);
    }

    // Mise à jour d'un projet
    @PutMapping("/{id}")
    public ResponseEntity<ProjetDTO> updateProjet(@PathVariable String id, @RequestBody ProjetDTO projetDTO) {
        ProjetDTO updatedProjet = projetService.updateProjet(id, projetDTO);
        if (updatedProjet != null) {
            return new ResponseEntity<>(updatedProjet, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Suppression d'un projet
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProjet(@PathVariable String id) {
        projetService.deleteProjet(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Suppression réussie, pas besoin de vérifier
    }
    @GetMapping("/kanban")
    public ResponseEntity<Map<EtatProjetEnum, List<Projet>>> getKanbanProjets() {
        Map<EtatProjetEnum, List<Projet>> kanban = kanbanProjetService.getProjetsParEtatKanban();
        return new ResponseEntity<>(kanban, HttpStatus.OK);
    }

    @PutMapping("/kanban/{id}")
    public ResponseEntity<Projet> updateEtatEtOrdreProjet(
            @PathVariable String id,
            @RequestParam EtatProjetEnum etat,
            @RequestParam Integer ordre) {
        Projet updatedProjet = kanbanProjetService.updateEtatEtOrdreProjet(id, etat, ordre);
        return new ResponseEntity<>(updatedProjet, HttpStatus.OK);
    }
    @PostMapping("/calculer-avancement")
    public ResponseEntity<String> calculerAvancementViaSwagger() {
        projetService.getAvancementProjet();
        return ResponseEntity.ok("Avancement recalculé pour tous les projets.");
    }
    @GetMapping("/{id}/risque-retard")
    public RisqueRetardDTO getRisqueRetard(@PathVariable String id) {
        double score = projetService.calculerScoreRisqueRetard(id);
        String interpretation = projetService.interpreterScoreRisque(score);
        return new RisqueRetardDTO(score, interpretation);
    }

    public static class RisqueRetardDTO {
        private double score;
        private String interpretation;

        public RisqueRetardDTO(double score, String interpretation) {
            this.score = score;
            this.interpretation = interpretation;
        }
        public double getScore() {
            return score;
        }
        public String getInterpretation() {
            return interpretation;
        }
    }

}
