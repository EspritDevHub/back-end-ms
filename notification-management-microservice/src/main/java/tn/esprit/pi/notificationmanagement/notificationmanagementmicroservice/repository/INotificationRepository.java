package tn.esprit.pi.notificationmanagement.notificationmanagementmicroservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import tn.esprit.pi.notificationmanagement.notificationmanagementmicroservice.Entities.Notification;

import java.util.List;

public interface INotificationRepository extends MongoRepository<Notification, String> {
    List<Notification> findByUserId(String userId);
}
