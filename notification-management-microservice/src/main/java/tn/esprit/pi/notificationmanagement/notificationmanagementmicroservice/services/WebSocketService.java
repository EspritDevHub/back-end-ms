package tn.esprit.pi.notificationmanagement.notificationmanagementmicroservice.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import tn.esprit.pi.notificationmanagement.notificationmanagementmicroservice.Dto.NotificationResponseDTO;
import tn.esprit.pi.notificationmanagement.notificationmanagementmicroservice.controllers.WebSocketContoller;
//this no
@Service
public class WebSocketService {

    private final SimpMessagingTemplate simpMessagingTemplate;

    public WebSocketService(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }


    public void sendNotification(NotificationResponseDTO notification) {
        simpMessagingTemplate.convertAndSend("/topic/notifications", notification);
    }
}
