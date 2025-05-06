package tn.esprit.pi.notemanagement.notemanagementmicroservice.Entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Enum.TypeNote;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document("notes")
@CompoundIndexes( {
        @CompoundIndex(name = "notes", def = "{'etudiantId' : 1, 'groupeId' : 1, 'seanceId' : 1}", unique = true)
})
public class Note {
    @Id
    private String id;
    private String seanceId;
    private String sprintId;
    private String critereId;
    private String etudiantId; // ou groupeId si note de groupe
    private String groupeId;
    private double valeur;
    private TypeNote typeNote;

}