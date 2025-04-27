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
    private Long id;
    private String titre;
    private String description;
    private String dateDebut;
    private String dateFin;

}


