package tn.esprit.pi.reclamationmanagement.reclamationmanagementmicroservice.FeignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import tn.esprit.pi.reclamationmanagement.reclamationmanagementmicroservice.Dto.NotificationRequestDTO;
import tn.esprit.pi.reclamationmanagement.reclamationmanagementmicroservice.Dto.NotificationResponseDTO;

@FeignClient(name = "notification-management-microservice", url = "http://localhost:8081")
public interface NotificationClient {
    @PostMapping("/api/notifications")
    NotificationResponseDTO createNotification(@RequestBody NotificationRequestDTO dto);

    @PostMapping("/api/notifications/sendWebSocketNotification")
    void sendWebSocketNotification(@RequestBody NotificationRequestDTO notification);

}
