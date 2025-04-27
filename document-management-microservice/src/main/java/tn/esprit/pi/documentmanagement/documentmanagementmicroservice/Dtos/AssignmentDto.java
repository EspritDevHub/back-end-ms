package tn.esprit.pi.documentmanagement.documentmanagementmicroservice.Dtos;

import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssignmentDto {

    private String id;
    private String seanceId;
    private String enseignantId;
    private String type;
    private String description;
    private Date dateLimite;
    private String statut;
}

