package tn.esprit.pi.seancemanagement.seancemanagementmicroservice.Entities;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import tn.esprit.pi.seancemanagement.seancemanagementmicroservice.Enum.TypeNote;

import java.util.Date;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document("seances")
public class Seance {
    @Id
    private String id;
    private String titre;
    private String description;
    private Long numero ;
    private Double note ;


    private String sprintId; // ID du sprint
    private List<String> critereIds; // Liste des critères d’évaluation
    private Date date;
    private TypeNote typeNote; // "groupe" ou "individuel"
}