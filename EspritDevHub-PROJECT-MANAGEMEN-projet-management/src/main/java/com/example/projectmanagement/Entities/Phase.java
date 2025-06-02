package com.example.projectmanagement.Entities;



import com.example.projectmanagement.Entities.Enums.EtatPhaseEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.LocalDate;
import java.util.List;

@Document(collection = "phases")
@Getter
@Setter
public class Phase {

    @Id
    private String id;
    private EtatPhaseEnum etat;

    private String nom;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String projetId;

    @DBRef(lazy = false) // charge immédiatement les sprints référencés
    private List<Sprint> sprints;

    // Constructeurs
    public Phase() {}

    public Phase(String nom, LocalDate dateDebut, LocalDate dateFin, List<Sprint> sprints) {
        this.nom = nom;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.sprints = sprints;
    }

    // Getters et Setters


    public void setId(String id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public List<Sprint> getSprints() {
        return sprints;
    }

    public EtatPhaseEnum getEtat() {
        return etat;
    }

    public void setEtat(EtatPhaseEnum etat) {
        this.etat = etat;
    }

    public void setSprints(List<Sprint> sprints) {
        this.sprints = sprints;
    }
}

