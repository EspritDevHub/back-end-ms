package tn.esprit.pi.documentmanagement.documentmanagementmicroservice.Mappers;

import org.springframework.stereotype.Component;
import tn.esprit.pi.documentmanagement.documentmanagementmicroservice.Dtos.DocumentDto;
import tn.esprit.pi.documentmanagement.documentmanagementmicroservice.Entities.Document;

@Component
public class DocumentMapper {

    // Convertir le DTO en Entité
    public Document toEntity(DocumentDto documentDTO) {
        Document document = new Document();
        document.setSeanceId(documentDTO.getSeanceId());
        document.setEtudiantId(documentDTO.getEtudiantId());
        document.setEnseignantId(documentDTO.getEnseignantId());
        document.setType(documentDTO.getType());
        document.setFichier(documentDTO.getFichier());
        document.setCommentaire(documentDTO.getCommentaire());
        document.setStatut(documentDTO.getStatut());
        return document;
    }

    // Convertir l'Entité en DTO
    public DocumentDto toDTO(Document document) {
        DocumentDto documentDTO = new DocumentDto();
        documentDTO.setSeanceId(document.getSeanceId());
        documentDTO.setEtudiantId(document.getEtudiantId());
        documentDTO.setEnseignantId(document.getEnseignantId());
        documentDTO.setType(document.getType());
        documentDTO.setFichier(document.getFichier());
        documentDTO.setCommentaire(document.getCommentaire());
        documentDTO.setStatut(document.getStatut());
        return documentDTO;
    }
}
