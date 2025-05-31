package tn.esprit.pi.documentmanagement.documentmanagementmicroservice.Dtos;

import lombok.Data;

import java.util.Date;

@Data
public class EvaluationDto {
    private String id;
    private String documentId;
    private String enseignantId;
    private Double note;
    private String commentaire;
    private String suggestion;
    private Date dateEvaluation;
    private String fichierEvaluationUrl;
}
