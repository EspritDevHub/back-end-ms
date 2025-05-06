package tn.esprit.pi.documentmanagement.documentmanagementmicroservice.Dtos;

import lombok.Data;
import tn.esprit.pi.documentmanagement.documentmanagementmicroservice.Enum.FileType;

import java.util.Date;


@Data
public class DocumentDto {
    private String id;
    private String assignmentId;
    private String seanceId;
    private FileType type;
    private String contenu;
    private String nomFichier;
    private String commentaire;
    private String statut;
    private Date dateSoumission;
    private Date dateLimite;
    private boolean modifiable;
}