package tn.esprit.pi.notemanagement.notemanagementmicroservice.Entities;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "cours")  // nom de ta collection MongoDB
public class Cours {
    @Id
    private String id;
    private String titre;
    private String description;
    private String lien;  // ton DTO avait "lien" mais dans ta DB tu as "url", il faut harmoniser !
    private String matiere;
    private String niveau;
}
