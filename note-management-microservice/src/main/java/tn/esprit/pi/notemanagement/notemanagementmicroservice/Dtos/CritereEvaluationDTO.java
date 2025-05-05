package tn.esprit.pi.notemanagement.notemanagementmicroservice.Dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CritereEvaluationDTO {
    private String id;
    private String nom;
    private String description;
    private double coefficient;
    private String sprintId;

}
