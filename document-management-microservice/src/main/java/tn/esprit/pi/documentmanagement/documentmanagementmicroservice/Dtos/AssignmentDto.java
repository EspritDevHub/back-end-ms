package tn.esprit.pi.documentmanagement.documentmanagementmicroservice.Dtos;

import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssignmentDto {
    private String titre;
    private String description;
    private String seanceId;
    private String typeRendu;
    private Date dateLimite;
}