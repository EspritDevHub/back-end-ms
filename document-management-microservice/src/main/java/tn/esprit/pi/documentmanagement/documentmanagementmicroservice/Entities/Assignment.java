package tn.esprit.pi.documentmanagement.documentmanagementmicroservice.Entities;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "assignments")
@Data
public class Assignment {
    @Id
    private String id;
    private String seanceId;
    private String enseignantId;
    private String type; // 'FICHIER', 'LIEN' ou 'TEXTE'
    private String description;
    private Date dateLimite;
    private String statut; // 'À_RENDRE', 'RENDU', 'EN_RETARD'
    private Date createdAt;
    private String createdBy;

    private String titre;
    private boolean actif = true;
}
