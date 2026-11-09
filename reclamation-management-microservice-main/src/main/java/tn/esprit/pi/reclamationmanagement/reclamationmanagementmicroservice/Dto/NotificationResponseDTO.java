package tn.esprit.pi.reclamationmanagement.reclamationmanagementmicroservice.Dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Builder
public class NotificationResponseDTO {
    private String id;
    private String title;
    private String message;
    private String userId;
    private boolean read;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
