package tn.esprit.pi.notificationmanagement.notificationmanagementmicroservice.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationDTO {
    private String id;
    private String title;
    private String message;
    private String userId;
    private boolean read;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
