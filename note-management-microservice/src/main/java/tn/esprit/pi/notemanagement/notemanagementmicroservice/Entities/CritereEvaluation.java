package tn.esprit.pi.notemanagement.notemanagementmicroservice.Entities;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document("criteres")
public class CritereEvaluation {
    @Id
    private String id;
    private String nom;
    private String description;
    private double coefficient;
    private String sprintId;

}
