package tn.esprit.pi.reclamationmanagement.reclamationmanagementmicroservice.Mappers;

import tn.esprit.pi.reclamationmanagement.reclamationmanagementmicroservice.Dto.ReclamationRequestDTO;
import tn.esprit.pi.reclamationmanagement.reclamationmanagementmicroservice.Dto.ReclamationResponseDTO;
import tn.esprit.pi.reclamationmanagement.reclamationmanagementmicroservice.Entities.Reclamation;
import tn.esprit.pi.reclamationmanagement.reclamationmanagementmicroservice.Enums.ReclamationStatus;

import java.time.format.DateTimeFormatter;

import static org.json.XMLTokener.entity;

public class ReclamationMapper {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Reclamation toEntity(ReclamationRequestDTO dto, String userId) {
        return Reclamation.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .status(ReclamationStatus.PENDING)
                .userId(userId)
                .createdAt(java.time.LocalDateTime.now())
                .image(dto.getImage())
                //.jiraTicketId(dto.getJiraTicketId())
                .build();
    }

    public static ReclamationResponseDTO toDTO(Reclamation entity) {
        return ReclamationResponseDTO.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .status(entity.getStatus().name())
                .userId(entity.getUserId())
                .createdAt(entity.getCreatedAt().format(formatter))
                .modifiedAt(entity.getModifiedAt() != null ? entity.getModifiedAt().format(formatter) : null)
                .image(entity.getImage())
                .jiraTicketId(entity.getJiraTicketId())
                .build();
    }
}
