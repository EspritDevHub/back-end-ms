package tn.esprit.pi.notemanagement.notemanagementmicroservice.Dtos;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CritereEvaluationRequestDTO {
    private String nom;
    private String description;
    private double coefficient;
    private String sprintId;
}
