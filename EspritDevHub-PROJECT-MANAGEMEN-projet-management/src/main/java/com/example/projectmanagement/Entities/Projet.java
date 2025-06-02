package com.example.projectmanagement.Entities;

import com.example.projectmanagement.Entities.Enums.EtapeProjetEnum;
import com.example.projectmanagement.Entities.Enums.EtatProjetEnum;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Document(collection = "projets") // La collection MongoDB où seront stockés les projets
@Data
@NoArgsConstructor
@Getter
@Setter
public class Projet {

    @Id
    private String id;              // Identifiant du projet

    private String code;            // Code du projet
    private String titre;           // Titre du projet
    private String description;     // Description du projet

    private EtatProjetEnum etat;// L'état Kanban du projet
    private Integer ordre;// Position dans la colonne Kanban
    private EtapeProjetEnum etapeProjet;  // Etape du projet (ex : "REALISATION")

    private LocalDate dateDebut;      // Date de début du projet
    private LocalDate dateFinPrevu;   // Date de fin prévue du projet
    @DBRef
    private Groupe groupe;         // Identifiant du groupe
    private boolean retard;
    private Double avancement;

    public boolean isRetard() {
        return retard;
    }



    // Liste des jalons (phases) directement dans l'entité Projet
    private List<Phase> jalons;      // Liste des jalons du projet

    private Long createdBy;        // Identifiant de l'utilisateur ayant créé le projet
    private LocalDate creationDate; // Date de création du projet

    public Projet(String id, String code, String titre, String description, EtatProjetEnum etat,
                  EtapeProjetEnum etapeProjet, LocalDate dateDebut, LocalDate dateFinPrevu,
                   Long createdBy,
                  LocalDate creationDate) {
        this.id = id;
        this.code = code;
        this.titre = titre;
        this.description = description;
        this.etat = etat;
        this.etapeProjet = etapeProjet;
        this.dateDebut = dateDebut;
        this.dateFinPrevu = dateFinPrevu;
        this.createdBy = createdBy;
        this.creationDate = creationDate;
    }
}
