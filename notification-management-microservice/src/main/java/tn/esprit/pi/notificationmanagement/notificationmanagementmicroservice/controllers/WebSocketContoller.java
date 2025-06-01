package tn.esprit.pi.notificationmanagement.notificationmanagementmicroservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pi.notificationmanagement.notificationmanagementmicroservice.Dto.NotificationResponseDTO;
import tn.esprit.pi.notificationmanagement.notificationmanagementmicroservice.services.NotificationService;
import tn.esprit.pi.notificationmanagement.notificationmanagementmicroservice.services.WebSocketService;
//this no
@RestController
@Controller
@RequestMapping("api/notifications")
@RequiredArgsConstructor
@CrossOrigin(
        origins = "http://localhost:4200", // ✅ correct origin
        allowedHeaders = "*",
        allowCredentials = "true", // ✅ if using cookies/tokens
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE}
)

public class WebSocketContoller {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    private final WebSocketService myWebSocketService;

    // Send message to a specific user
  /*  @MessageMapping("/sendNotification")
    public void sendNotification(NotificationResponseDTO notification) {
        messagingTemplate.convertAndSend("/topic/notifications", notification);  // Broadcast to all subscribers
    }*/
    public void broadcastNotification(NotificationResponseDTO notification) {
        messagingTemplate.convertAndSend("/topic/notifications", notification);
    }
    @PostMapping("/sendWebSocketNotification")
    public ResponseEntity<String> sendWebSocketNotification(@RequestBody NotificationResponseDTO notification) {
        if (notification == null) {
            return ResponseEntity.badRequest().body("Notification data is missing.");
        }
        try {
            myWebSocketService.sendNotification(notification); // Send the full object
            return ResponseEntity.ok("Notification sent successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sending notification: " + e.getMessage());
        }
    }
    }


