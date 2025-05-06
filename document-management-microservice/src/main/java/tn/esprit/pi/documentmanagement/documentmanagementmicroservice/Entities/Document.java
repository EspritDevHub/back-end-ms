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
    private String assignmentId;  // Référence à l'assignment
    private String seanceId;      // Référence à la séance
    private String etudiantId;    // Référence à l'étudiant
    private FileType type;        // Type de rendu (FICHIER, LIEN, TEXTE)
    private String contenu;       // URL ou texte selon le type
    private String nomFichier;    // Nom original du fichier (si type=FICHIER)
    private String commentaire;
    private String statut;        // BROUILLON, SOUMIS, CORRIGE
    private Date dateSoumission;
    private Date dateLimite;      // Copie de la date limite de l'assignment
    private Date createdAt;
    private Date updatedAt;
}
