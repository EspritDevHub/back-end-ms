package tn.esprit.pi.notemanagement.notemanagementmicroservice.Entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document("notes")
public class Note {
    @Id
    private String id;
    private String seanceId;
    private String sprintId;
    private String critereId;
    private String etudiantId; // ou groupeId si note de groupe
    private String groupeId;
    private double valeur;

}