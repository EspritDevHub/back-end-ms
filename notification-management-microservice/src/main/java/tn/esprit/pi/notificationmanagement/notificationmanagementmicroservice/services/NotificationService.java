package tn.esprit.pi.notificationmanagement.notificationmanagementmicroservice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.pi.notificationmanagement.notificationmanagementmicroservice.Dto.NotificationDTO;
import tn.esprit.pi.notificationmanagement.notificationmanagementmicroservice.Dto.NotificationRequestDTO;
import tn.esprit.pi.notificationmanagement.notificationmanagementmicroservice.Dto.NotificationResponseDTO;
import tn.esprit.pi.notificationmanagement.notificationmanagementmicroservice.Entities.Notification;
import tn.esprit.pi.notificationmanagement.notificationmanagementmicroservice.mapper.NotificationMapper;
import tn.esprit.pi.notificationmanagement.notificationmanagementmicroservice.repository.INotificationRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final INotificationRepository repository;
    private final NotificationMapper mapper;
    private final SimpMessagingTemplate messagingTemplate;




    public NotificationDTO createNotification(NotificationDTO dto) {
        dto.setCreatedAt(LocalDateTime.now());
        dto.setUpdatedAt(LocalDateTime.now());
        Notification saved = repository.save(mapper.toEntity(dto));
        NotificationDTO result = mapper.toDto(saved);
        messagingTemplate.convertAndSend("/topic/notifications/" + result.getUserId(), result);
        return result;
    }

    public List<NotificationDTO> getNotificationsByUserId(String userId) {
        return repository.findByUserId(userId)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public NotificationDTO updateNotification(String id, NotificationDTO dto) {
        Notification existing = repository.findById(id).orElseThrow();
        existing.setTitle(dto.getTitle());
        existing.setMessage(dto.getMessage());
        existing.setRead(dto.isRead());
        existing.setUpdatedAt(LocalDateTime.now());
        Notification updated = repository.save(existing);
        return mapper.toDto(updated);
    }

    @Transactional
    public NotificationDTO deleteNotification(String id) {
        Notification notification = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found with ID: " + id));

        repository.deleteById(id);

        return mapper.toDto(notification);
    }

    public List<NotificationDTO> getAllNotifications() {
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
}
