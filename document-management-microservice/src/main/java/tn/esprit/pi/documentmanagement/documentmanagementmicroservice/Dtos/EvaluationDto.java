package tn.esprit.pi.documentmanagement.documentmanagementmicroservice.Dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
@AllArgsConstructor
@Data
public class EvaluationDto {
    private String id;
    private String documentId;
    private String enseignantId;
    private Double note;
    private String commentaire;
    private String suggestion;
    private Date dateEvaluation;
    private String lienGit;

    public EvaluationDto(Double note, String commentaire, String suggestion) {
        this.note = note;
        this.commentaire = commentaire;
        this.suggestion = suggestion;
    } public EvaluationDto() {
    }


}


