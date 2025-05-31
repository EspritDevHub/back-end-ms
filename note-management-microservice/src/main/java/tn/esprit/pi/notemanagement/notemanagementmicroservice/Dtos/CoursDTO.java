package tn.esprit.pi.notemanagement.notemanagementmicroservice.Dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CoursDTO {
    private String id;          // id du cours
    private String titre;       // titre du cours
    private String description; // description du cours
    private String lien;        // lien URL vers le cours
    private String matiere;     // la matière ou le nom du cours (pour filtrer par rapport au nom de la séance)
    private String niveau;

}
