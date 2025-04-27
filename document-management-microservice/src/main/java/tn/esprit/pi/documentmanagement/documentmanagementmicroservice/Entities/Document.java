package tn.esprit.pi.documentmanagement.documentmanagementmicroservice.Entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import tn.esprit.pi.documentmanagement.documentmanagementmicroservice.Enum.FileType;

import javax.persistence.PrePersist;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@org.springframework.data.mongodb.core.mapping.Document(collection = "documents")
public class Document {

    @Id
    private String id;

    private String seanceId;      // Référence à la séance
    private String etudiantId;    // Référence à l'étudiant
    private String enseignantId;  // Référence à l'enseignant
    private FileType type;        // Type de fichier (PDF, Image, Lien, etc.)
    private String fichier;       // Lien ou chemin du fichier
    private String commentaire;   // Commentaire ajouté par l'étudiant
    private String statut;        // Statut (Soumis, En cours, Terminé)
    private Date createdAt;       // Date de création
    private String createdBy;     // ID de l'utilisateur qui a créé ce travail (enseignant ou étudiant)

}
