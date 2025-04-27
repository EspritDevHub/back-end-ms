package tn.esprit.pi.documentmanagement.documentmanagementmicroservice.Entities;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document("assignments")
public class Assignment {

    private String id;                // Identifiant unique de l'Assignment
    private String seanceId;          // ID de la séance à laquelle le travail est assigné
    private String enseignantId;      // ID du professeur qui a créé l'Assignment
    private String type;              // Type du travail (lien, fichier, etc.)
    private String description;       // Description du travail à rendre
    private Date dateLimite;          // Date limite de dépôt du travail
    private String statut;            // Statut (à faire, en cours, terminé)
    private Date createdAt;           // Date de création
    private String createdBy;         // ID de l'utilisateur qui a créé l'Assignment (professeur)
}
