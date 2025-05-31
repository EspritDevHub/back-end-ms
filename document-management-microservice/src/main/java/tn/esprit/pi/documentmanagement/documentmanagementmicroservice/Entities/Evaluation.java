package tn.esprit.pi.documentmanagement.documentmanagementmicroservice.Entities;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "evaluations")
public class Evaluation {
    @Id
    private String id;

    private String documentId;      // Référence au document soumis
    private String enseignantId;    // Correcteur
    private Double note;            // Note attribuée
    private String commentaire;     // Commentaire de l'enseignant
    private Date dateEvaluation;    // Date de correction

    private String fichierEvaluationUrl; // URL d’un fichier PDF ou ZIP joint (évaluation complémentaire)
}

