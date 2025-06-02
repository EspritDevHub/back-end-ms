package com.example.projectmanagement.Entities;

import com.example.projectmanagement.Dtos.TacheDTO;
import com.example.projectmanagement.Entities.Enums.EtatTacheEnum;
import com.example.projectmanagement.Entities.Enums.TypeDureeEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "taches")
@Data
@NoArgsConstructor
@Getter
@Setter
public class Tache {

    @Id
    private String id;

    private String titre;                   // Titre de la tâche
    private String description;             // Description courte
    private String assigneA;                // Personne assignée à la tâche
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private LocalDateTime dateDebut;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")// Date de début prévue
    private LocalDateTime dateFin;          // Date de fin prévue

    private EtatTacheEnum etat;             // État (NON_COMMENCE, EN_COURS, TERMINE)
    private Integer avancement;             // Pourcentage d'avancement (ex: 75)

    private Float duree;                    // Durée estimée (ex: 3.5)
    private TypeDureeEnum typeDuree;//(heure, jour)
    private String projetId;
    private String sprintId;
    public Tache(TacheDTO dto) {
        this.id = dto.getId();
        this.titre = dto.getTitre();
        this.description = dto.getDescription();
        this.assigneA = dto.getAssigneA();
        this.dateDebut = dto.getDateDebut();
        this.dateFin = dto.getDateFin();
        this.etat = dto.getEtat();
        this.avancement = dto.getAvancement();
        this.duree = dto.getDuree();
        this.typeDuree = dto.getTypeDuree();
    }
    // Constructeur de copie
    public Tache(Tache other) {
        this.id = other.id;
        this.titre = other.titre;
        this.description = other.description;
        this.assigneA = other.assigneA;
        this.dateDebut = other.dateDebut;
        this.dateFin = other.dateFin;
        this.etat = other.etat;
        this.avancement = other.avancement;
        this.duree = other.duree;
        this.typeDuree = other.typeDuree;
        this.projetId = other.projetId;
        this.sprintId = other.sprintId;
    }
}
