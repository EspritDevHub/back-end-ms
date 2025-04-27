package tn.esprit.pi.notemanagement.notemanagementmicroservice.Dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SprintDTO {
    private String id; // ID du sprint
    private String nom; // Nom du sprint
    private Date dateDebut;
    private Date dateFin;

}

