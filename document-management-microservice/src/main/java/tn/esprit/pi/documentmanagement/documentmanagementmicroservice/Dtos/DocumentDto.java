package tn.esprit.pi.documentmanagement.documentmanagementmicroservice.Dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tn.esprit.pi.documentmanagement.documentmanagementmicroservice.Entities.Document;
import tn.esprit.pi.documentmanagement.documentmanagementmicroservice.Enum.FileType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentDto {


    private String seanceId;
    private String etudiantId;
    private String enseignantId;
    private FileType type;
    private String fichier;
    private String commentaire;
    private String statut;
}
