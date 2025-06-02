package com.example.projectmanagement.Dtos;

import com.example.projectmanagement.Entities.Enums.EtatTacheEnum;
import com.example.projectmanagement.Entities.Enums.TypeDureeEnum;
import com.example.projectmanagement.Entities.Tache;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Data



public class TacheDTO {

    private String id;
    private String titre;
    private String description;
    private String assigneA;


    private LocalDateTime dateDebut;


    private LocalDateTime dateFin;

    private EtatTacheEnum etat;
    private Integer avancement;
    private Float duree;
    private TypeDureeEnum typeDuree;
    private String projetId;

    // ✅ Constructeur vide requis par Jackson pour la désérialisation
    public TacheDTO() {
    }

    // ✅ Constructeur pour convertir une entité Tache en DTO
    public TacheDTO(Tache tache) {
        this.id = tache.getId();
        this.titre = tache.getTitre();
        this.description = tache.getDescription();
        this.assigneA = tache.getAssigneA();
        this.dateDebut = tache.getDateDebut();
        this.dateFin = tache.getDateFin();
        this.etat = tache.getEtat();
        this.avancement = tache.getAvancement();
        this.duree = tache.getDuree();
        this.typeDuree = tache.getTypeDuree();
        this.projetId = tache.getProjetId();
    }

}
