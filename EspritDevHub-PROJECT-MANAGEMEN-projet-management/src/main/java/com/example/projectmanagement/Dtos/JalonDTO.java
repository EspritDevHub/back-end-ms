package com.example.projectmanagement.Dtos;

import com.example.projectmanagement.Entities.Enums.EtatProjetEnum;
import lombok.Data;
import java.time.LocalDate;

@Data
public class JalonDTO {

    private String nom;             // Nom du jalon (phase), ex: "Analyse", "Développement", etc.
    private LocalDate dateDebut;    // Date de début du jalon
    private LocalDate dateFin;      // Date de fin du jalon
    private EtatProjetEnum statut;  // Statut du jalon, lié à EtatProjetEnum (ex: "EN_ATTENTE", "EN_COURS", "TERMINE")
    private String remarque;        // Remarque concernant ce jalon

    // Constructeur
    public JalonDTO(String nom, LocalDate dateDebut, LocalDate dateFin, EtatProjetEnum statut, String remarque) {
        this.nom = nom;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.statut = statut;
        this.remarque = remarque;
    }

    // Constructeur pour transformer un objet Jalon en DTO
    public JalonDTO(com.example.projectmanagement.Entities.Jalon jalon) {
        this.nom = jalon.getNom();
        this.dateDebut = jalon.getDateDebut();
        this.dateFin = jalon.getDateFin();
        this.statut = jalon.getStatut();
        this.remarque = jalon.getRemarque();
    }
}
