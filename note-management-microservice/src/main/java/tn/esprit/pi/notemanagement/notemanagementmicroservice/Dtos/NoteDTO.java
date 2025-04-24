package tn.esprit.pi.notemanagement.notemanagementmicroservice.Dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoteDTO {
    private String id;
    private String seanceId;
    private String sprintId;
    private String critereId;
    private String etudiantId;
    private String groupeId;
    private double valeur;
}
