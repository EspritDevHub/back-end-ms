package tn.esprit.pi.notificationmanagement.notificationmanagementmicroservice.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.pi.notificationmanagement.notificationmanagementmicroservice.Dto.NotificationDTO;
import tn.esprit.pi.notificationmanagement.notificationmanagementmicroservice.Entities.Notification;

@Component
public class NotificationMapper {
    public NotificationDTO toDto(Notification entity) {
        return NotificationDTO.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .message(entity.getMessage())
                .userId(entity.getUserId())
                .read(entity.isRead())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public Notification toEntity(NotificationDTO dto) {
        return Notification.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .message(dto.getMessage())
                .userId(dto.getUserId())
                .read(dto.isRead())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }



}


