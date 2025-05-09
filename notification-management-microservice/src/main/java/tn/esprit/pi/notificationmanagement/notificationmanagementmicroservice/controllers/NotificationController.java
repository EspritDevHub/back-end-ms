package tn.esprit.pi.notificationmanagement.notificationmanagementmicroservice.controllers;

import org.springframework.web.bind.annotation.RequestBody;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pi.notificationmanagement.notificationmanagementmicroservice.Dto.NotificationDTO;
import tn.esprit.pi.notificationmanagement.notificationmanagementmicroservice.Dto.NotificationRequestDTO;
import tn.esprit.pi.notificationmanagement.notificationmanagementmicroservice.Dto.NotificationResponseDTO;
import tn.esprit.pi.notificationmanagement.notificationmanagementmicroservice.services.NotificationService;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class NotificationController {
    private final NotificationService service;


    @PostMapping
    public NotificationDTO create(@RequestBody NotificationDTO dto) {
        return service.createNotification(dto);
    }

    @GetMapping("/user/{userId}")
    public List<NotificationDTO> getByUser(@PathVariable String userId) {
        return service.getNotificationsByUserId(userId);
    }

    @PutMapping("/{id}")
    public NotificationDTO update(@PathVariable String id, @RequestBody NotificationDTO dto) {
        return service.updateNotification(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.deleteNotification(id);
    }

    @GetMapping
    public List<NotificationDTO> getAll() {
        return service.getAllNotifications();
    }
}
