package com.example.projectmanagement.Entities;

import com.example.projectmanagement.Entities.Enums.EtatProjetEnum;
import lombok.Data;
import java.time.LocalDate;

@Data
public class Jalon {

    private String nom;              // Nom du jalon (phase), ex: "Analyse", "Développement", etc.
    private LocalDate dateDebut;     // Date de début du jalon
    private LocalDate dateFin;       // Date de fin du jalon
    private EtatProjetEnum statut;   // Statut du jalon lié à EtatProjetEnum (ex: "EN_ATTENTE", "EN_COURS", "TERMINE")
    private String remarque;         // Remarque concernant ce jalon

    // Constructeur
    public Jalon(String nom, LocalDate dateDebut, LocalDate dateFin, EtatProjetEnum statut, String remarque) {
        this.nom = nom;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.statut = statut;
        this.remarque = remarque;
    }
}
